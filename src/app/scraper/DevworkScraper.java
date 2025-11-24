package app.scraper;

import com.google.gson.Gson;

import app.model.bean.JobDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DevworkScraper {

  private static final String[] USER_AGENTS = {
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
  };
  private static final String[] REFERERS = { "https://www.google.com", "https://www.bing.com",
      "https://duckduckgo.com" };

  private final Gson gson = new Gson();

  public JobDetail scrape(String url) throws IOException {
    System.out.println("[DevworkScraper] Starting to scrape URL: " + url);
    Document doc = Jsoup.connect(url)
        .userAgent(USER_AGENTS[new Random().nextInt(USER_AGENTS.length)])
        .referrer(REFERERS[new Random().nextInt(REFERERS.length)])
        .timeout(30000)
        .get();

    Element headerDetails = doc.selectFirst("div.header-details");
    if (headerDetails == null) {
      System.out.println("[DevworkScraper] ERROR: header-details not found for URL: " + url);
      throw new IOException("Failed to scrape the page.");
    }

    String[] companyInfo = getCompany(headerDetails);
    String companyName = companyInfo[0];
    String companyUrl = companyInfo[1];
    if (companyUrl == null) {
      throw new IOException("Cannot get the company's information.");
    }

    String thumbnail = getThumbnail(doc);
    String province = getProvince(headerDetails);
    String salary = getSalary(doc);
    Map<String, String> jobInfo = getJobInfo(doc);
    String[] skillsAndDescriptions = getDescriptions(doc);
    String skillsJson = skillsAndDescriptions[0];
    String descriptionsJson = skillsAndDescriptions[1];
    String title = getJobTitle(headerDetails);

    JobDetail job = new JobDetail();
    job.setUrl(url);
    job.setThumbnail(thumbnail);
    job.setJobTitle(title);
    job.setProvince(province);
    job.setSkills(skillsJson);
    job.setDescriptions(descriptionsJson);
    job.setJobInfo(gson.toJson(jobInfo));
    job.setCompanyName(companyName);
    job.setCompanyUrl(companyUrl);
    job.setSalary(salary);

    System.out.println("[DevworkScraper] Successfully scraped job: " + title + " from " + url);
    return job;
  }

  private String[] getDescriptions(Document doc) {
    try {
      Element mainElement = doc.selectFirst("div.background-content-job");
      Elements titleElements = mainElement != null ? mainElement.select("h2.block-title") : new Elements();
      Elements descriptionElements = mainElement != null ? mainElement.select("div.block-desc") : new Elements();

      Element tagsElement = mainElement != null ? mainElement.selectFirst("div.tags") : null;
      Elements skillElements = new Elements();
      if (tagsElement != null) {
        try {
          skillElements = tagsElement.select("a[href^=/viec-lam/]");
        } catch (Exception e) {
          System.err.println("[DevworkScraper] Error selecting skills: " + e.getMessage());
        }
      }

      String[] skills = skillElements.stream().map(Element::text).toArray(String[]::new);

      Map<String, String> descriptions = new HashMap<>();
      for (int i = 1; i < titleElements.size() && i - 1 < descriptionElements.size(); i++) {
        String title = removeConsecutiveSpaces(titleElements.get(i).text());
        String desc = removeConsecutiveSpaces(descriptionElements.get(i - 1).text());
        descriptions.put(title, desc);
      }

      return new String[] { gson.toJson(skills), gson.toJson(descriptions) };
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String[] { "[]", "{}" };
  }

  private Map<String, String> getJobInfo(Document doc) {
    Map<String, String> result = new HashMap<>();
    Elements infoElements = doc.select("div.location-profile-job");
    for (Element element : infoElements) {
      String title = removeConsecutiveSpaces(element.selectFirst("strong").text());
      String desc = removeConsecutiveSpaces(element.selectFirst("span").text());
      result.put(title, desc);
    }
    return result;
  }

  private String getSalary(Document doc) {
    Elements divs = doc.select("div.mt-12");
    for (Element div : divs) {
      if (div.selectFirst("div.salary-amount") != null) {
        return removeConsecutiveSpaces(div.selectFirst("div").text());
      }
    }
    return null;
  }

  private String getProvince(Element headerDetails) {
    Elements ps = headerDetails.select("p");
    for (Element p : ps) {
      if (p.selectFirst("i") != null) {
        return removeConsecutiveSpaces(p.text());
      }
    }
    return null;
  }

  private String[] getCompany(Element headerDetails) {
    Element companyTitleElement = headerDetails.selectFirst("h5.mb-10.fw-400");
    if (companyTitleElement == null) {
      return new String[] { null, null };
    }
    Element a = companyTitleElement.selectFirst("a");
    if (a != null) {
      String companyUrl = "https://devwork.com" + a.attr("href");
      String companyName = removeConsecutiveSpaces(a.text());
      return new String[] { companyName, companyUrl };
    }
    return new String[] { null, null };
  }

  private String getThumbnail(Document doc) {
    Element headerImage = doc.selectFirst("div.header-image");
    if (headerImage != null) {
      Element img = headerImage.selectFirst("img");
      if (img != null) {
        return img.attr("src");
      }
    }
    return null;
  }

  private String getJobTitle(Element headerDetails) {
    Element h1 = headerDetails.selectFirst("h1.mb-3");
    return h1 != null ? removeConsecutiveSpaces(h1.text()) : null;
  }

  private String removeConsecutiveSpaces(String text) {
    // return text.replaceAll("\\s+", " ").trim();
    return text;
  }
}
