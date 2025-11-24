// Chức năng người dùng chỉnh sửa việc làm đã crawl của họ

package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.JobDetail;
import app.model.bean.ScrapeJob;
import app.model.bean.User;
import app.model.bo.JobDetailBo;
import app.model.bo.ScrapeJobBo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/editJob")
public class EditJobServlet extends HttpServlet {

  private final JobDetailBo jobDetailBo = new JobDetailBo();
  private final ScrapeJobBo scrapeJobBo = new ScrapeJobBo();
  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    // Check ownership (Admin có quyền chỉnh sửa việc làm của bất kì ai)
    ScrapeJob scrapeJob = scrapeJobBo.findById(jobDetail.getScrapeJobId());
    if (scrapeJob == null || (scrapeJob.getUserId() != user.getId() && !"admin".equals(user.getRole()))) {
      resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
      return;
    }

    // Parse complex fields
    List<String> skills = null;
    try {
      skills = gson.fromJson(jobDetail.getSkills(), new TypeToken<List<String>>() {
      }.getType());
    } catch (Exception e) {
      skills = List.of();
    }

    Map<String, String> descriptions = null;
    try {
      descriptions = gson.fromJson(jobDetail.getDescriptions(), new TypeToken<Map<String, String>>() {
      }.getType());
    } catch (Exception e) {
      descriptions = Map.of();
    }

    Map<String, String> jobInfo = null;
    try {
      jobInfo = gson.fromJson(jobDetail.getJobInfo(), new TypeToken<Map<String, String>>() {
      }.getType());
    } catch (Exception e) {
      jobInfo = Map.of();
    }

    req.setAttribute("jobDetail", jobDetail);
    req.setAttribute("skills", skills);
    req.setAttribute("descriptions", descriptions);
    req.setAttribute("jobInfo", jobInfo);
    req.getRequestDispatcher("/editJob.jsp").forward(req, resp);
  }

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

    // Update simple fields
    jobDetail.setJobTitle(req.getParameter("jobTitle"));
    jobDetail.setCompanyName(req.getParameter("companyName"));
    jobDetail.setProvince(req.getParameter("province"));
    jobDetail.setSalary(req.getParameter("salary"));
    jobDetail.setUrl(req.getParameter("url"));
    jobDetail.setCompanyUrl(req.getParameter("companyUrl"));
    jobDetail.setThumbnail(req.getParameter("thumbnail"));

    // Handle skills (one per line)
    String skillsParam = req.getParameter("skills");
    List<String> skills = List.of();
    if (skillsParam != null && !skillsParam.trim().isEmpty()) {
      skills = List.of(skillsParam.split("\\n")).stream()
          .map(String::trim)
          .filter(s -> !s.isEmpty())
          .toList();
    }
    jobDetail.setSkills(gson.toJson(skills));

    // Handle descriptions (key-value pairs)
    // Assuming form sends descKeys[] and descValues[]
    String[] descKeys = req.getParameterValues("descKeys");
    String[] descValues = req.getParameterValues("descValues");
    Map<String, String> descriptions = Map.of();
    if (descKeys != null && descValues != null && descKeys.length == descValues.length) {
      Map<String, String> descMap = new java.util.HashMap<>();
      for (int i = 0; i < descKeys.length; i++) {
        if (!descKeys[i].trim().isEmpty()) {
          descMap.put(descKeys[i].trim(), descValues[i].trim());
        }
      }
      descriptions = descMap;
    }
    jobDetail.setDescriptions(gson.toJson(descriptions));

    // Handle jobInfo similarly
    String[] infoKeys = req.getParameterValues("infoKeys");
    String[] infoValues = req.getParameterValues("infoValues");
    Map<String, String> jobInfo = Map.of();
    if (infoKeys != null && infoValues != null && infoKeys.length == infoValues.length) {
      Map<String, String> infoMap = new java.util.HashMap<>();
      for (int i = 0; i < infoKeys.length; i++) {
        if (!infoKeys[i].trim().isEmpty()) {
          infoMap.put(infoKeys[i].trim(), infoValues[i].trim());
        }
      }
      jobInfo = infoMap;
    }
    jobDetail.setJobInfo(gson.toJson(jobInfo));

    jobDetailBo.update(jobDetail);
    resp.sendRedirect("myJobs");
  }
}
