package app.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import app.model.bean.ScrapeJob;

public class ScrapeJobDao {

  public List<ScrapeJob> findByUserId(int userId) {
    List<ScrapeJob> jobs = new ArrayList<>();
    String sql = "SELECT * FROM scrape_job WHERE user_id = ? ORDER BY created_at DESC";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        ScrapeJob job = new ScrapeJob();
        job.setId(rs.getInt("id"));
        job.setUserId(rs.getInt("user_id"));
        job.setStatus(rs.getString("status"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setUpdatedAt(rs.getTimestamp("updated_at"));
        job.setErrorMessage(rs.getString("error_message"));
        job.setTotalPages(rs.getInt("total_pages"));
        job.setScrapedCount(rs.getInt("scraped_count"));
        jobs.add(job);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return jobs;
  }

  public ScrapeJob findById(int id) {
    String sql = "SELECT * FROM scrape_job WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        ScrapeJob job = new ScrapeJob();
        job.setId(rs.getInt("id"));
        job.setUserId(rs.getInt("user_id"));
        job.setStatus(rs.getString("status"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setUpdatedAt(rs.getTimestamp("updated_at"));
        job.setErrorMessage(rs.getString("error_message"));
        job.setTotalPages(rs.getInt("total_pages"));
        job.setScrapedCount(rs.getInt("scraped_count"));
        return job;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public int save(ScrapeJob job) {
    String sql = "INSERT INTO scrape_job (user_id, status, total_pages, scraped_count, created_at, updated_at, error_message) VALUES (?, ?, ?, ?, NOW(), NOW(), ?)";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setInt(1, job.getUserId());
      stmt.setString(2, job.getStatus());
      stmt.setInt(3, job.getTotalPages());
      stmt.setInt(4, job.getScrapedCount());
      stmt.setString(5, job.getErrorMessage());
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public void updateStatus(int id, String status) {
    String sql = "UPDATE scrape_job SET status = ?, updated_at = NOW() WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, status);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void updateErrorMessage(int id, String errorMessage) {
    String sql = "UPDATE scrape_job SET error_message = ?, updated_at = NOW() WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, errorMessage);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void updateTotalPages(int id, int totalPages) {
    String sql = "UPDATE scrape_job SET total_pages = ?, updated_at = NOW() WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, totalPages);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void incrementScrapedCount(int id) {
    String sql = "UPDATE scrape_job SET scraped_count = scraped_count + 1, updated_at = NOW() WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void updateScrapedCount(int id, int count) {
    String sql = "UPDATE scrape_job SET scraped_count = ?, updated_at = NOW() WHERE id = ?";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, count);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<ScrapeJob> findAll() {
    List<ScrapeJob> jobs = new ArrayList<>();
    String sql = "SELECT * FROM scrape_job ORDER BY created_at DESC";
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        ScrapeJob job = new ScrapeJob();
        job.setId(rs.getInt("id"));
        job.setUserId(rs.getInt("user_id"));
        job.setStatus(rs.getString("status"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setUpdatedAt(rs.getTimestamp("updated_at"));
        job.setErrorMessage(rs.getString("error_message"));
        job.setTotalPages(rs.getInt("total_pages"));
        job.setScrapedCount(rs.getInt("scraped_count"));
        jobs.add(job);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return jobs;
  }
}
