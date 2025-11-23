package app.model.bo;

import app.model.bean.ScrapeJob;
import app.model.dao.ScrapeJobDao;

import java.util.List;

public class ScrapeJobBo {
    private final ScrapeJobDao scrapeJobDao = new ScrapeJobDao();

    public List<ScrapeJob> findByUserId(int userId) {
        return scrapeJobDao.findByUserId(userId);
    }

    public ScrapeJob findById(int id) {
        return scrapeJobDao.findById(id);
    }

    public int save(ScrapeJob job) {
        return scrapeJobDao.save(job);
    }

    public void updateTotalPages(int id, int totalPages) {
        scrapeJobDao.updateTotalPages(id, totalPages);
    }

    public void incrementScrapedCount(int id) {
        scrapeJobDao.incrementScrapedCount(id);
    }

    public void updateScrapedCount(int id, int count) {
        scrapeJobDao.updateScrapedCount(id, count);
    }

    public void updateStatus(int id, String status) {
        scrapeJobDao.updateStatus(id, status);
    }

    public void updateErrorMessage(int id, String msg) {
        scrapeJobDao.updateErrorMessage(id, msg);
    }

    public List<ScrapeJob> findAll() {
        return scrapeJobDao.findAll();
    }
}
