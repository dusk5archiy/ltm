package app.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import app.model.bean.User;
import app.model.bean.ScrapeJob;
import app.model.bo.ScrapeJobBo;
import app.model.bo.UserBo;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import app.model.dao.DBUtil;
import java.util.List;

@WebServlet(urlPatterns = "/statistics")
public class StatisticsServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      resp.sendRedirect("login");
      return;
    }

    User user = (User) session.getAttribute("user");
    if (!"admin".equals(user.getRole())) {
      resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
      return;
    }

    String format = req.getParameter("format");

    // Get statistics
    int totalUsers = getTotalUsers();
    int totalScrapeJobs = getTotalScrapeJobs();
    int totalJobDetails = getTotalJobDetails();

    if ("json".equals(format)) {
      resp.setContentType("application/json");
      resp.setHeader("Cache-Control", "no-cache");
      resp.getWriter().write("{\"totalUsers\":" + totalUsers + ",\"totalScrapeJobs\":" + totalScrapeJobs + ",\"totalJobDetails\":" + totalJobDetails + "}");
      return;
    }

    if ("jobs".equals(format)) {
      ScrapeJobBo scrapeJobBo = new ScrapeJobBo();
      UserBo userBo = new UserBo();
      List<ScrapeJob> allJobs = scrapeJobBo.findAll();
      Gson gson = new Gson();
      StringBuilder json = new StringBuilder("[");
      for (int i = 0; i < allJobs.size(); i++) {
        ScrapeJob job = allJobs.get(i);
        User jobUser = userBo.findById(job.getUserId()).orElse(null);
        String username = jobUser != null ? jobUser.getUsername().replace("\"", "\\\"") : "Unknown";
        json.append("{\"id\":").append(job.getId()).append(",\"username\":\"").append(username).append("\",\"status\":\"").append(job.getStatus()).append("\",\"createdAt\":\"").append(job.getCreatedAt()).append("\",\"scrapedCount\":").append(job.getScrapedCount()).append(",\"totalPages\":").append(job.getTotalPages()).append(",\"errorMessage\":\"").append(job.getErrorMessage() != null ? job.getErrorMessage().replace("\"", "\\\"") : "").append("\"}");
        if (i < allJobs.size() - 1) json.append(",");
      }
      json.append("]");
      resp.setContentType("application/json");
      resp.setHeader("Cache-Control", "no-cache");
      resp.getWriter().write(json.toString());
      return;
    }

    req.setAttribute("totalUsers", totalUsers);
    req.setAttribute("totalScrapeJobs", totalScrapeJobs);
    req.setAttribute("totalJobDetails", totalJobDetails);

    req.getRequestDispatcher("/statistics.jsp").forward(req, resp);
  }

  private int getTotalUsers() {
    try (Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM user")) {
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private int getTotalScrapeJobs() {
    try (Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM scrape_job")) {
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private int getTotalJobDetails() {
    try (Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM job_detail")) {
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
