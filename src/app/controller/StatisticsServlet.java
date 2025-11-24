package app.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import app.model.bean.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import app.model.dao.DBUtil;

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

    // Get statistics
    int totalUsers = getTotalUsers();
    int totalScrapeJobs = getTotalScrapeJobs();
    int totalJobDetails = getTotalJobDetails();

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
