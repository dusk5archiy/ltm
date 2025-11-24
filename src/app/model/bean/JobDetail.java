package app.model.bean;

import java.sql.Timestamp;

public class JobDetail {
  private int id;
  private int scrapeJobId;
  private String url;
  private String thumbnail;
  private String jobTitle;
  private String companyUrl;
  private String companyName;
  private String province;
  private String salary;
  private String skills; // JSON string
  private String descriptions; // JSON string
  private String jobInfo; // JSON string
  private Timestamp collectedAt;

  public JobDetail() {
  }

  public JobDetail(int id, int scrapeJobId, String url, String thumbnail, String jobTitle,
      String companyUrl, String companyName, String province, String salary,
      String skills, String descriptions, String jobInfo, Timestamp collectedAt) {
    this.id = id;
    this.scrapeJobId = scrapeJobId;
    this.url = url;
    this.thumbnail = thumbnail;
    this.jobTitle = jobTitle;
    this.companyUrl = companyUrl;
    this.companyName = companyName;
    this.province = province;
    this.salary = salary;
    this.skills = skills;
    this.descriptions = descriptions;
    this.jobInfo = jobInfo;
    this.collectedAt = collectedAt;
  }

  // Getters and setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getScrapeJobId() {
    return scrapeJobId;
  }

  public void setScrapeJobId(int scrapeJobId) {
    this.scrapeJobId = scrapeJobId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getCompanyUrl() {
    return companyUrl;
  }

  public void setCompanyUrl(String companyUrl) {
    this.companyUrl = companyUrl;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getSkills() {
    return skills;
  }

  public void setSkills(String skills) {
    this.skills = skills;
  }

  public String getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(String descriptions) {
    this.descriptions = descriptions;
  }

  public String getJobInfo() {
    return jobInfo;
  }

  public void setJobInfo(String jobInfo) {
    this.jobInfo = jobInfo;
  }

  public Timestamp getCollectedAt() {
    return collectedAt;
  }

  public void setCollectedAt(Timestamp collectedAt) {
    this.collectedAt = collectedAt;
  }
}
