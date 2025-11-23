package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.ScrapeJob;
import app.model.bean.User;
import app.model.bo.ScrapeJobBo;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/progress")
public class JobProgressServlet extends HttpServlet {
    private final ScrapeJobBo scrapeJobBo = new ScrapeJobBo();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing job id");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid job id");
            return;
        }

        ScrapeJob job = scrapeJobBo.findById(id);
        if (job == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Job not found");
            return;
        }
        // Authorization: only owner can query their job
        java.util.Optional<User> currentUser = java.util.Optional.ofNullable((User) req.getSession(false).getAttribute("user"));
        if (!currentUser.isPresent() || job.getUserId() != currentUser.get().getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this job");
            return;
        }

        Map<String, Object> out = new HashMap<>();
        out.put("id", job.getId());
        out.put("status", job.getStatus());
        out.put("scrapedCount", job.getScrapedCount());
        out.put("totalPages", job.getTotalPages());

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(out));
    }
}
