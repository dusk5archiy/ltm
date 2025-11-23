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
}
