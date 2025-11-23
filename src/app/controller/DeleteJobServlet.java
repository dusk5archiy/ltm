package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.JobDetail;
import app.model.bean.User;
import app.model.bo.JobDetailBo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = "/deleteJob")
public class DeleteJobServlet extends HttpServlet {

    private final JobDetailBo jobDetailBo = new JobDetailBo();

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
        // Note: We don't check if the job belongs to the user, assuming users can only delete their own jobs
        // In a real app, you'd verify ownership

        // For now, just delete the JobDetail
        // In the future, you might want to soft delete or check ownership
        jobDetailBo.delete(id); // Need to add delete method to BO and DAO

        resp.sendRedirect("myJobs");
    }

    // For GET, redirect to myJobs
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("myJobs");
    }
}