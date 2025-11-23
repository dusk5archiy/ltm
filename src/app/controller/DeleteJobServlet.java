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

@WebServlet(urlPatterns = "/deleteJob")
public class DeleteJobServlet extends HttpServlet {

    private final JobDetailBo jobDetailBo = new JobDetailBo();
    private final ScrapeJobBo scrapeJobBo = new ScrapeJobBo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect("myJobs");
            return;
        }

        int id = Integer.parseInt(idParam);
        JobDetail jobDetail = jobDetailBo.findById(id);
        if (jobDetail == null) {
            resp.sendRedirect("myJobs");
            return;
        }

        // Check ownership
        ScrapeJob scrapeJob = scrapeJobBo.findById(jobDetail.getScrapeJobId());
        if (scrapeJob == null || (scrapeJob.getUserId() != user.getId() && !"admin".equals(user.getRole()))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        jobDetailBo.delete(id);

        resp.sendRedirect("myJobs");
    }

    // For GET, redirect to myJobs
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("myJobs");
    }
}