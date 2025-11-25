package app.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.concurrent.*;

import app.model.bean.JobDetail;

public class ScrapeManager {

  private final DevworkScraper scraper = new DevworkScraper();

  public List<JobDetail> scrapeUrls(List<String> urls, Consumer<JobDetail> onScraped) {
    List<JobDetail> results = new ArrayList<>();
    System.out.println("[ScrapeManager] Starting to scrape " + urls.size() + " URLs");
    // ExecutorService executor = Executors.newFixedThreadPool(Math.min(urls.size(),
    // 4));
    ExecutorService executor = Executors.newFixedThreadPool(3); // 1 FOR EXPERIMENTRAL

    final CompletionService<JobDetail> completionService = new ExecutorCompletionService<>(executor);
    int submitted = 0;
    for (String url : urls) {
      completionService.submit(() -> scrapeSingle(url));
      submitted++;
    }

    for (int i = 0; i < submitted; i++) {
      try {
        Future<JobDetail> future = completionService.take();
        JobDetail result = future.get();
        if (result != null) {
          if (onScraped != null) {
            try {
              onScraped.accept(result);
            } catch (Exception ex) {
              System.err.println("[ScrapeManager] onScraped handler failed: " + ex.getMessage());
            }
          }
          results.add(result);
        }
      } catch (InterruptedException | ExecutionException e) {
        System.err.println("[ScrapeManager] Error processing future: " + e.getMessage());
      }
    }

    executor.shutdown();
    System.out.println("[ScrapeManager] Finished scraping. Results: " + results.size() + " jobs scraped successfully");
    return results;
  }

  private JobDetail scrapeSingle(String url) {
    System.out.println("[ScrapeManager] Scraping single URL: " + url);
    try {
      JobDetail result = scraper.scrape(url);
      System.out.println("[ScrapeManager] Successfully scraped: " + url);
      return result;
    } catch (IOException e) {
      System.err.println("[ScrapeManager] Failed to scrape: " + url + " - " + e.getMessage());
      return null;
    }
  }
}
