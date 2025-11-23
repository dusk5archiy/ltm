package app.controller;

import app.model.bean.ScrapeJob;
import app.model.bean.User;
import app.model.bo.ScrapeJobBo;
import app.util.ProgressBroadcaster;
import com.google.gson.Gson;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/progress-sse", asyncSupported = true)
public class JobProgressSseServlet extends HttpServlet {
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
        // Authorization
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this job");
            return;
        }
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || job.getUserId() != currentUser.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to view this job");
            return;
        }

        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        AsyncContext ac = req.startAsync();
        ac.setTimeout(0);
    final PrintWriter writer = ac.getResponse().getWriter();
    ac.addListener(new AsyncListener() {
            @Override public void onComplete(AsyncEvent event) { /* nothing */ }
            @Override public void onTimeout(AsyncEvent event) { cleanup(ac); }
            @Override public void onError(AsyncEvent event) { cleanup(ac); }
            @Override public void onStartAsync(AsyncEvent event) { /* nothing */ }
            private void cleanup(AsyncContext ac) {
                try {
                    ProgressBroadcaster.getInstance().removeListener(id, writer);
                    try { writer.close(); } catch (Exception ignore) {}
                } catch (Exception ignore) {}
            }
        });

        // Register listener
        ProgressBroadcaster.getInstance().addListener(id, writer);

        // Send initial state
        Map<String, Object> out = new HashMap<>();
        out.put("id", job.getId());
        out.put("status", job.getStatus());
        out.put("scrapedCount", job.getScrapedCount());
        out.put("totalPages", job.getTotalPages());
        writer.write("data: " + gson.toJson(out) + "\n\n");
        writer.flush();
    }
}
