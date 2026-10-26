CREATE DATABASE IF NOT EXISTS college;
USE college;

CREATE TABLE IF NOT EXISTS student_results (
    roll_no VARCHAR(20) PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL,
    branch VARCHAR(30) NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    subject1 DECIMAL(5,2) NOT NULL,
    subject2 DECIMAL(5,2) NOT NULL,
    subject3 DECIMAL(5,2) NOT NULL,
    subject4 DECIMAL(5,2) NOT NULL,
    subject5 DECIMAL(5,2) NOT NULL,
    total DECIMAL(6,2) NOT NULL,
    percentage DECIMAL(5,2) NOT NULL,
    grade VARCHAR(2) NOT NULL,
    result_status VARCHAR(10) NOT NULL
);
