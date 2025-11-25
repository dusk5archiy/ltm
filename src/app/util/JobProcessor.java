package app.util;

import app.model.bean.JobDetail;
import app.model.bean.ScrapeJob;
import app.model.bo.JobDetailBo;
import app.model.bo.ScrapeJobBo;
import app.scraper.ScrapeManager;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobProcessor {
  private static JobProcessor instance;
  private BlockingQueue<ScrapeTask> queue = new LinkedBlockingQueue<>();
  private Thread worker;
  private ScrapeManager scrapeManager = new ScrapeManager();
  private ScrapeJobBo scrapeJobBo = new ScrapeJobBo();
  private JobDetailBo jobDetailBo = new JobDetailBo();

  private JobProcessor() {
    worker = new Thread(this::processQueue);
    worker.setDaemon(true);
    worker.start();
  }

  public static synchronized JobProcessor getInstance() {
    if (instance == null) {
      instance = new JobProcessor();
    }
    return instance;
  }

  public void addTask(ScrapeTask task) {
    queue.offer(task);
  }

  private void processQueue() {
    while (true) {
      try {
        ScrapeTask task = queue.take();
        processTask(task);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  private void processTask(ScrapeTask task) {
    ScrapeJob job = task.getJob();
    List<String> urls = task.getUrls();
    System.out.println("[JobProcessor] Starting to process job ID: " + job.getId() + " with " + urls.size() + " URLs");

    try {
      scrapeJobBo.updateStatus(job.getId(), "running");
      // Ensure total pages is recorded (in case job was created without total)
      if (job.getTotalPages() == 0) {
        scrapeJobBo.updateTotalPages(job.getId(), urls.size());
      }
      List<JobDetail> results = scrapeManager.scrapeUrls(urls, detail -> {
        detail.setScrapeJobId(job.getId());
        jobDetailBo.save(detail);
        scrapeJobBo.incrementScrapedCount(job.getId());
      });
      if (results.isEmpty()) {
        throw new Exception("No data scraped from provided URLs. Please check if URLs are valid Devwork job pages.");
      }

      scrapeJobBo.updateStatus(job.getId(), "completed");
      System.out.println(
          "[JobProcessor] Job ID " + job.getId() + " completed successfully with " + results.size() + " results");
    } catch (Exception e) {
      String errorMsg = e.getMessage() != null ? e.getMessage() : "Unknown error occurred during scraping";
      scrapeJobBo.updateErrorMessage(job.getId(), errorMsg);
      scrapeJobBo.updateStatus(job.getId(), "failed");
      System.err.println("[JobProcessor] Job ID " + job.getId() + " failed: " + errorMsg);
      e.printStackTrace();
    }
  }
}
