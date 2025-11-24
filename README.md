# Ứng dụng Thu thập Việc làm Devwork

Một ứng dụng Java Servlet full-stack thu thập thông tin việc làm từ Devwork.vn, lưu trữ trong MySQL và cung cấp giao diện web để người dùng quản lý các công việc thu thập dữ liệu.

## Tính năng

- Xác thực người dùng (đăng nhập/đăng ký/đăng xuất)
- Bảng điều khiển để xem lịch sử công việc thu thập
- Quản lý công việc cá nhân (xem, tạo, chỉnh sửa, xóa công việc thu thập)
- Tạo công việc thu thập mới với nhiều URL
- Xử lý công việc theo hàng đợi với việc thu thập nền
- Theo dõi tiến độ công việc thu thập theo thời gian thực (SSE)
- Hiển thị chi tiết công việc đã thu thập với kỹ năng, mô tả và thông tin công ty
- Thống kê quản trị (cho người dùng admin)
- Giao diện web đáp ứng với chế độ xem chi tiết công việc rõ ràng
- Hỗ trợ đa ngôn ngữ (tiếng Việt) với mã hóa UTF-8

## Kiến trúc

Ứng dụng này tuân theo kiến trúc MVC (Model-View-Controller). Dưới đây là các sơ đồ kiến trúc chi tiết cho từng chức năng chính:

### 1. Luồng Xác thực (Authentication Flow)

```mermaid
%%{init: {"flowchart": {"curve": "basis"}}}%%
graph TD
    subgraph "Views"
        V2[login.jsp]
        V3[register.jsp]
    end

    subgraph "Controllers"
        C1[LoginServlet]
        C2[LogoutServlet]
    end

    subgraph "Models"
        M1[User]
        M4[UserDao]
    end

    subgraph "Database"
        DB1[(user)]
    end

    V2 --> C1
    V3 --> C1
    C1 --> M4
    C2 --> M4
    M4 --> DB1
```

### 2. Luồng Quản lý Công việc (Job Management Flow)

```mermaid
%%{init: {"flowchart": {"curve": "basis"}}}%%
graph TD
    subgraph "Views"
        V4[dashboard.jsp]
        V5[myJobs.jsp]
        V6[createJob.jsp]
        V7[editJob.jsp]
        V8[jobDetails.jsp]
        V9[jobView.jsp]
    end

    subgraph "Controllers"
        C3[DashboardServlet]
        C4[MyJobsServlet]
        C5[CreateJobServlet]
        C6[EditJobServlet]
        C7[DeleteJobServlet]
        C8[JobDetailsServlet]
        C9[JobViewServlet]
    end

    subgraph "Models"
        M2[ScrapeJob]
        M5[ScrapeJobDao]
    end

    subgraph "Business Logic"
        P1[JobProcessor]
    end

    subgraph "Database"
        DB2[(scrape_job)]
    end

    V4 --> C3
    V5 --> C4
    V6 --> C5
    V7 --> C6
    V8 --> C8
    V9 --> C9

    C3 --> M5
    C4 --> M5
    C5 --> M5
    C6 --> M5
    C7 --> M5
    C8 --> M5
    C9 --> M5

    C5 --> P1
    C6 --> P1

    M5 --> DB2
```

### 3. Luồng Xử lý Công việc (Job Processing Flow)

```mermaid
%%{init: {"flowchart": {"curve": "basis"}}}%%
graph TD
    subgraph "Controllers"
        C5[CreateJobServlet]
        C6[EditJobServlet]
        C10[JobProgressServlet]
    end

    subgraph "Business Logic"
        P1[JobProcessor]
        T1[ScrapeTask]
        PB1[ProgressBroadcaster]
        S1[DevworkScraper]
        S2[ScrapeManager]
    end

    C5 --> P1
    C6 --> P1
    C10 --> PB1

    P1 --> T1
    P1 --> PB1
    P1 --> S2
    S2 --> S1
```

### 4. Luồng Truy cập Dữ liệu (Data Access Flow)

```mermaid
%%{init: {"flowchart": {"curve": "basis"}}}%%
graph TD
    subgraph "Models"
        M1[User]
        M2[ScrapeJob]
        M3[JobDetail]
        M4[UserDao]
        M5[ScrapeJobDao]
        M6[JobDetailDao]
        M7[DBUtil]
    end

    subgraph "Database"
        DB1[(user)]
        DB2[(scrape_job)]
        DB3[(job_detail)]
    end

    M4 --> DB1
    M5 --> DB2
    M6 --> DB3
```

### 5. Luồng Tính năng Admin (Admin Features Flow)

```mermaid
%%{init: {"flowchart": {"curve": "basis"}}}%%
graph TD
    subgraph "Views"
        V10[statistics.jsp]
    end

    subgraph "Controllers"
        C11[StatisticsServlet]
    end

    subgraph "Models"
        M1[User]
        M2[ScrapeJob]
        M4[UserDao]
        M5[ScrapeJobDao]
    end

    subgraph "Database"
        DB1[(user)]
        DB2[(scrape_job)]
    end

    V10 --> C11
    C11 --> M4
    C11 --> M5
    M4 --> DB1
    M5 --> DB2
```

### Các thành phần

- **Model**: Các lớp Java đại diện cho dữ liệu (User, ScrapeJob, JobDetail) và DAO để thao tác cơ sở dữ liệu (UserDao, ScrapeJobDao, JobDetailDao)
- **View**: Các trang JSP cho giao diện người dùng (index.jsp, login.jsp, register.jsp, dashboard.jsp, myJobs.jsp, createJob.jsp, editJob.jsp, jobDetails.jsp, jobView.jsp, statistics.jsp)
- **Controller**: Các Servlet xử lý yêu cầu và phản hồi HTTP (LoginServlet, DashboardServlet, MyJobsServlet, CreateJobServlet, EditJobServlet, DeleteJobServlet, JobDetailsServlet, StatisticsServlet, v.v.)
- **Scraper**: Bộ thu thập web dựa trên Jsoup cho Devwork.vn (DevworkScraper, ScrapeManager)
- **Job Processor**: Hệ thống hàng đợi nền để xử lý các tác vụ thu thập (JobProcessor, ScrapeTask, ProgressBroadcaster)
- **Database**: MySQL với mã hóa UTF-8 để lưu trữ người dùng, công việc và dữ liệu đã thu thập (user, scrape_job, job_detail)

## Yêu cầu tiên quyết

- Java JDK 11 hoặc cao hơn
- Apache Maven 3.6+
- Apache Tomcat 9+ (Windows)
- MySQL 8.0+ (qua XAMPP)
- Hệ điều hành Windows

## Cài đặt và Triển khai

### 1. Cài đặt Java JDK

1. Tải JDK từ [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
2. Cài đặt vào vị trí mặc định (ví dụ: `C:\Program Files\Java\jdk-11.x.x`)
3. Thêm `%JAVA_HOME%\bin` vào PATH (JAVA_HOME là tùy chọn nhưng được khuyến nghị cho một số công cụ phát triển)

### 2. Cài đặt Apache Maven

1. Tải Maven từ [maven.apache.org](https://maven.apache.org/download.cgi)
2. Giải nén vào `C:\Program Files\Apache\maven-3.x.x`
3. Thêm `%MAVEN_HOME%\bin` vào PATH (MAVEN_HOME là tùy chọn nhưng được khuyến nghị cho một số công cụ phát triển)
4. Xác minh: Mở command prompt và chạy `mvn -version`

### 3. Cài đặt XAMPP (MySQL)

1. Tải XAMPP từ [apachefriends.org](https://www.apachefriends.org/download.html)
2. Cài đặt vào vị trí mặc định (thường là `C:\xampp`)
3. Khởi động XAMPP Control Panel
4. Khởi động dịch vụ MySQL
5. Mở phpMyAdmin (<http://localhost/phpmyadmin>)
6. Tạo cơ sở dữ liệu: `ltm` với collation `utf8mb4_vietnamese_ci`

### 4. Cài đặt Apache Tomcat

1. Tải Tomcat 9 từ [tomcat.apache.org](https://tomcat.apache.org/download-90.cgi)
2. Giải nén vào `C:\Program Files\Apache\Tomcat 9.x`
3. Thiết lập biến môi trường:
   - `CATALINA_HOME = C:\Program Files\Apache\Tomcat 9.x`
4. Khởi động Tomcat: Chạy `%CATALINA_HOME%\bin\startup.bat`

### 5. Thiết lập Cơ sở dữ liệu

1. Trong phpMyAdmin, tạo cơ sở dữ liệu `ltm` với collation `utf8mb4_vietnamese_ci`
2. Chạy script SQL từ `schema.sql` trong thư mục gốc dự án

### 6. Xây dựng và Triển khai Ứng dụng

1. Clone hoặc tải về dự án
2. Mở command prompt trong thư mục gốc dự án
3. Xây dựng dự án:

   ```
   mvn clean package
   ```

4. Triển khai file WAR:
   - Sao chép `target\app.war` vào `%CATALINA_HOME%\webapps\`
   - Hoặc sử dụng Tomcat Manager (<http://localhost:8080/manager/html>)
5. Truy cập ứng dụng tại: <http://localhost:8080/app/>

## Cách sử dụng

1. **Đăng ký/Đăng nhập**: Tạo tài khoản hoặc đăng nhập vào hệ thống
2. **Trang chủ**: Xem danh sách các công việc việc làm có sẵn
3. **Bảng điều khiển**: Xem lịch sử công việc thu thập của bạn
4. **Công việc của tôi**: Quản lý các công việc thu thập cá nhân (xem, chỉnh sửa, xóa)
5. **Tạo công việc**: Nhập URL công việc Devwork (một URL mỗi dòng) để thu thập
6. **Theo dõi tiến độ**: Xem tiến độ thu thập theo thời gian thực
7. **Xem kết quả**: Nhấp vào các công việc đã hoàn thành để xem thông tin chi tiết
8. **Chi tiết công việc**: Xem tiêu đề công việc, công ty, lương, kỹ năng (dưới dạng thẻ), mô tả và thông tin bổ sung
9. **Thống kê** (Admin): Xem thống kê tổng quan về hệ thống (cho người dùng admin)

## Công nghệ được sử dụng

- **Backend**: Java Servlets (Jakarta EE 6.1.0), Maven
- **Frontend**: JSP, HTML, CSS
- **Database**: MySQL 8.0 với hỗ trợ UTF-8
- **Scraping**: Jsoup 1.17.1
- **JSON Processing**: Gson 2.10.1
- **Web Server**: Apache Tomcat 9
- **Development**: Java 11, Maven

## Cấu trúc dự án

```
project-root/
├── src/
│   └── app/
│       ├── controller/     # Bộ điều khiển HTTP (Servlets)
│       ├── model/
│       │   ├── bean/       # Các lớp mô hình dữ liệu (User, ScrapeJob, JobDetail)
│       │   ├── bo/         # Đối tượng nghiệp vụ
│       │   └── dao/        # Đối tượng truy cập dữ liệu (UserDao, ScrapeJobDao, JobDetailDao, DBUtil)
│       ├── scraper/        # Logic thu thập web (DevworkScraper, ScrapeManager)
│       └── util/           # Tiện ích (JobProcessor, ScrapeTask, ProgressBroadcaster)
├── webapp/                 # Views JSP và tài nguyên web
│   ├── *.jsp               # Mẫu views
│   └── WEB-INF/
├── target/                 # Đầu ra build
├── schema.sql              # Lược đồ cơ sở dữ liệu
├── deprecated_sql/         # Các file SQL đã lỗi thời (giữ để tham khảo)
├── pom.xml                 # Cấu hình Maven
└── README.md               # File hiện tại mà bạn đang đọc
```

## Khắc phục sự cố

- **Xung đột cổng**: Đảm bảo các cổng Tomcat (8080) và MySQL (3306) không bị chiếm
- **Vấn đề mã hóa**: Cơ sở dữ liệu phải sử dụng collation UTF-8 cho văn bản tiếng Việt
- **Lỗi thu thập**: Kiểm tra URL có phải là trang công việc Devwork hợp lệ
- **Lỗi build**: Đảm bảo Java và Maven đã được thêm vào PATH. Nếu gặp vấn đề với một số IDE hoặc công cụ, hãy thiết lập JAVA_HOME và MAVEN_HOME
