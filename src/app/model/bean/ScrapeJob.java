package app.model.bean;

import java.sql.Timestamp;

public class ScrapeJob {
    private int id;
    private int userId;
    private String status; // pending, running, completed, failed
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String errorMessage;
    private int totalPages;
    private int scrapedCount;

    public ScrapeJob() {}

    public ScrapeJob(int id, int userId, String status, Timestamp createdAt, Timestamp updatedAt, String errorMessage, int totalPages, int scrapedCount) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.errorMessage = errorMessage;
        this.totalPages = totalPages;
        this.scrapedCount = scrapedCount;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getScrapedCount() { return scrapedCount; }
    public void setScrapedCount(int scrapedCount) { this.scrapedCount = scrapedCount; }
}