// Giao diện tạo tiến trình crawl trang web

package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.ScrapeJob;
import app.model.bean.User;
import app.model.bo.ScrapeJobBo;
import app.util.JobProcessor;
import app.util.ScrapeTask;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/createJob")
public class CreateJobServlet extends HttpServlet {

  private final ScrapeJobBo scrapeJobBo = new ScrapeJobBo();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("user");
    if (user == null) {
      resp.sendRedirect("login");
      return;
    }
    req.getRequestDispatcher("/createJob.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    User user = (User) session.getAttribute("user");
    if (user == null) {
      resp.sendRedirect("login");
      return;
    }

    String urlsParam = req.getParameter("urls");

    if (urlsParam == null || urlsParam.trim().isEmpty()) {
      req.setAttribute("error", "Nhập các URL.");
      req.getRequestDispatcher("/createJob.jsp").forward(req, resp);
      return;
    }

    List<String> urls = Arrays.asList(urlsParam.split("\\r?\\n")).stream()
        .map(String::trim)
        .filter(url -> !url.isEmpty())
        .collect(Collectors.toList());

    if (urls.isEmpty()) {
      req.setAttribute("error", "Nhập ít nhất một URL hợp lệ.");
      req.getRequestDispatcher("/createJob.jsp").forward(req, resp);
      return;
    }

    // Validate URLs are from devwork.vn
    for (String url : urls) {
      if (!url.startsWith("https://devwork.vn/")) {
        req.setAttribute("error", "Chỉ chấp nhận URL từ devwork.vn. URL không hợp lệ: " + url);
        req.getRequestDispatcher("/createJob.jsp").forward(req, resp);
        return;
      }
    }

    ScrapeJob job = new ScrapeJob();
    job.setUserId(user.getId());
    job.setStatus("pending");
    job.setTotalPages(urls.size());
    job.setScrapedCount(0);

    int jobId = scrapeJobBo.save(job);
    if (jobId == -1) {
      req.setAttribute("error", "Không thể tạo tiến trình crawl.");
      req.getRequestDispatcher("/createJob.jsp").forward(req, resp);
      return;
    }
    job.setId(jobId);

    // Add to queue
    JobProcessor.getInstance().addTask(new ScrapeTask(job, urls));

    resp.sendRedirect("dashboard");
  }
}
