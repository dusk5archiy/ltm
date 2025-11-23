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
import java.util.List;

@WebServlet(urlPatterns = "/jobDetails")
public class JobDetailsServlet extends HttpServlet {

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

        String jobIdParam = req.getParameter("id");
        if (jobIdParam == null) {
            resp.sendRedirect("dashboard");
            return;
        }

        int jobId = Integer.parseInt(jobIdParam);
    ScrapeJob job = scrapeJobBo.findById(jobId);
        if (job == null || job.getUserId() != user.getId()) {
            resp.sendRedirect("dashboard");
            return;
        }

    List<JobDetail> details = jobDetailBo.findByScrapeJobId(jobId);
        req.setAttribute("job", job);
        req.setAttribute("details", details);
        req.getRequestDispatcher("/jobDetails.jsp").forward(req, resp);
    }
}