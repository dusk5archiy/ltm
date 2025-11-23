DROP DATABASE IF EXISTS ltm;
CREATE DATABASE ltm CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci;
USE ltm;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci;

CREATE TABLE IF NOT EXISTS scrape_job (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    status ENUM('pending', 'running', 'completed', 'failed') DEFAULT 'pending',
    total_pages INT DEFAULT 0,
    scraped_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    error_message TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci;

CREATE TABLE IF NOT EXISTS job_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    scrape_job_id INT NOT NULL,
    url TEXT NOT NULL,
    thumbnail TEXT,
    job_title TEXT,
    company_url TEXT,
    company_name TEXT,
    province TEXT,
    salary TEXT,
    skills JSON,
    descriptions JSON,
    job_info JSON,
    collected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (scrape_job_id) REFERENCES scrape_job(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci;

INSERT INTO user (username, password) VALUES
('admin', 'admin'),
('user1', 'user1'),
('user2', 'user2');
