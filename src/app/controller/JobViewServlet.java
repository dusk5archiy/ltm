package app.controller;

import jakarta.servlet.annotation.WebServlet;

import app.model.bean.JobDetail;
import app.model.bo.JobDetailBo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/jobView")
public class JobViewServlet extends HttpServlet {

  private final JobDetailBo jobDetailBo = new JobDetailBo();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String idParam = req.getParameter("id");
    if (idParam == null) {
      resp.sendRedirect("/");
      return;
    }

    int id = Integer.parseInt(idParam);
    JobDetail jobDetail = jobDetailBo.findById(id);
    if (jobDetail == null) {
      resp.sendRedirect("/");
      return;
    }

    req.setAttribute("jobDetail", jobDetail);
    req.getRequestDispatcher("/jobView.jsp").forward(req, resp);
  }
}
