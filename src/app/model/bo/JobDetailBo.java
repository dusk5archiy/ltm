package app.model.bo;

import app.model.bean.JobDetail;
import app.model.dao.JobDetailDao;

import java.util.List;

public class JobDetailBo {
    private final JobDetailDao jobDetailDao = new JobDetailDao();

    public List<JobDetail> findByScrapeJobId(int scrapeJobId) {
        return jobDetailDao.findByScrapeJobId(scrapeJobId);
    }

    public void save(JobDetail detail) {
        jobDetailDao.save(detail);
    }

    public List<JobDetail> findAll() {
        return jobDetailDao.findAll();
    }

    public JobDetail findById(int id) {
        return jobDetailDao.findById(id);
    }

    public void delete(int id) {
        jobDetailDao.delete(id);
    }

    public void update(JobDetail detail) {
        jobDetailDao.update(detail);
    }
}
