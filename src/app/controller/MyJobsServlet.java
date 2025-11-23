package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.JobDetail;
import app.model.bean.ScrapeJob;
import app.model.bean.User;
import app.model.bo.JobDetailBo;
import app.model.bo.ScrapeJobBo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/myJobs")
public class MyJobsServlet extends HttpServlet {

    private final ScrapeJobBo scrapeJobBo = new ScrapeJobBo();
    private final JobDetailBo jobDetailBo = new JobDetailBo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        List<ScrapeJob> completedJobs;
        if ("admin".equals(user.getRole())) {
            // Admins see all completed jobs
            List<ScrapeJob> allJobs = scrapeJobBo.findAll();
            completedJobs = allJobs.stream()
                .filter(job -> "completed".equals(job.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        } else {
            // Regular users see only their own jobs
            List<ScrapeJob> userJobs = scrapeJobBo.findByUserId(user.getId());
            completedJobs = userJobs.stream()
                .filter(job -> "completed".equals(job.getStatus()))
                .collect(java.util.stream.Collectors.toList());
        }

        // Get all job details for completed jobs
        List<JobDetail> jobDetails = new ArrayList<>();
        for (ScrapeJob job : completedJobs) {
            List<JobDetail> details = jobDetailBo.findByScrapeJobId(job.getId());
            jobDetails.addAll(details);
        }

        req.setAttribute("jobDetails", jobDetails);
        req.getRequestDispatcher("/myJobs.jsp").forward(req, resp);
    }
}