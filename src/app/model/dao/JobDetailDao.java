package app.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import app.model.bean.JobDetail;

public class JobDetailDao {

    public List<JobDetail> findByScrapeJobId(int scrapeJobId) {
        List<JobDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM job_detail WHERE scrape_job_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scrapeJobId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JobDetail detail = new JobDetail();
                detail.setId(rs.getInt("id"));
                detail.setScrapeJobId(rs.getInt("scrape_job_id"));
                detail.setUrl(rs.getString("url"));
                detail.setThumbnail(rs.getString("thumbnail"));
                detail.setJobTitle(rs.getString("job_title"));
                detail.setCompanyUrl(rs.getString("company_url"));
                detail.setCompanyName(rs.getString("company_name"));
                detail.setProvince(rs.getString("province"));
                detail.setSalary(rs.getString("salary"));
                detail.setSkills(rs.getString("skills"));
                detail.setDescriptions(rs.getString("descriptions"));
                detail.setJobInfo(rs.getString("job_info"));
                detail.setCollectedAt(rs.getTimestamp("collected_at"));
                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public List<JobDetail> findAll() {
        List<JobDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM job_detail ORDER BY collected_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JobDetail detail = new JobDetail();
                detail.setId(rs.getInt("id"));
                detail.setScrapeJobId(rs.getInt("scrape_job_id"));
                detail.setUrl(rs.getString("url"));
                detail.setThumbnail(rs.getString("thumbnail"));
                detail.setJobTitle(rs.getString("job_title"));
                detail.setCompanyUrl(rs.getString("company_url"));
                detail.setCompanyName(rs.getString("company_name"));
                detail.setProvince(rs.getString("province"));
                detail.setSalary(rs.getString("salary"));
                detail.setSkills(rs.getString("skills"));
                detail.setDescriptions(rs.getString("descriptions"));
                detail.setJobInfo(rs.getString("job_info"));
                detail.setCollectedAt(rs.getTimestamp("collected_at"));
                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public JobDetail findById(int id) {
        String sql = "SELECT * FROM job_detail WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JobDetail detail = new JobDetail();
                detail.setId(rs.getInt("id"));
                detail.setScrapeJobId(rs.getInt("scrape_job_id"));
                detail.setUrl(rs.getString("url"));
                detail.setThumbnail(rs.getString("thumbnail"));
                detail.setJobTitle(rs.getString("job_title"));
                detail.setCompanyUrl(rs.getString("company_url"));
                detail.setCompanyName(rs.getString("company_name"));
                detail.setProvince(rs.getString("province"));
                detail.setSalary(rs.getString("salary"));
                detail.setSkills(rs.getString("skills"));
                detail.setDescriptions(rs.getString("descriptions"));
                detail.setJobInfo(rs.getString("job_info"));
                detail.setCollectedAt(rs.getTimestamp("collected_at"));
                return detail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(JobDetail detail) {
        String sql = "INSERT INTO job_detail (scrape_job_id, url, thumbnail, job_title, company_url, company_name, province, salary, skills, descriptions, job_info, collected_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getScrapeJobId());
            stmt.setString(2, detail.getUrl());
            stmt.setString(3, detail.getThumbnail());
            stmt.setString(4, detail.getJobTitle());
            stmt.setString(5, detail.getCompanyUrl());
            stmt.setString(6, detail.getCompanyName());
            stmt.setString(7, detail.getProvince());
            stmt.setString(8, detail.getSalary());
            stmt.setString(9, detail.getSkills());
            stmt.setString(10, detail.getDescriptions());
            stmt.setString(11, detail.getJobInfo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM job_detail WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(JobDetail detail) {
        String sql = "UPDATE job_detail SET url = ?, thumbnail = ?, job_title = ?, company_url = ?, company_name = ?, province = ?, salary = ?, skills = ?, descriptions = ?, job_info = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, detail.getUrl());
            stmt.setString(2, detail.getThumbnail());
            stmt.setString(3, detail.getJobTitle());
            stmt.setString(4, detail.getCompanyUrl());
            stmt.setString(5, detail.getCompanyName());
            stmt.setString(6, detail.getProvince());
            stmt.setString(7, detail.getSalary());
            stmt.setString(8, detail.getSkills());
            stmt.setString(9, detail.getDescriptions());
            stmt.setString(10, detail.getJobInfo());
            stmt.setInt(11, detail.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}