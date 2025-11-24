package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.ScrapeJob;
import app.model.bean.User;
import app.model.bo.ScrapeJobBo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {

  private final ScrapeJobBo scrapeJobBo = new ScrapeJobBo();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("user");
    if (user == null) {
      resp.sendRedirect("login");
      return;
    }

    List<ScrapeJob> jobs = scrapeJobBo.findByUserId(user.getId());
    req.setAttribute("jobs", jobs);
    req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
  }
}
