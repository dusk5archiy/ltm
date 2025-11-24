package app.util;

import java.util.List;

import app.model.bean.ScrapeJob;

public class ScrapeTask {
  private ScrapeJob job;
  private List<String> urls;

  public ScrapeTask(ScrapeJob job, List<String> urls) {
    this.job = job;
    this.urls = urls;
  }

  public ScrapeJob getJob() {
    return job;
  }

  public List<String> getUrls() {
    return urls;
  }
}
