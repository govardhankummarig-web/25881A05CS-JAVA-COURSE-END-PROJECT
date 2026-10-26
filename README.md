# Student Results and Grade Management System

A simple Java + MySQL project for managing student records, subject marks, totals, percentages, grades, and pass/fail results. The repository also contains a LaTeX course-end project report based on the supplied report template.

## Features
- Create the student results table
- Insert student marks and details
- View all student results
- Search for a student by roll number
- Update marks and automatically recalculate total, percentage, grade, and status
- Delete a student record
- Parameterized database operations using JDBC `PreparedStatement`

## Requirements
- JDK 8 or newer
- MySQL Server
- MySQL Connector/J added to the Java classpath
- Overleaf or a LaTeX installation to compile the report

## Database setup
Run the SQL in `database/schema.sql` using MySQL. Update the database username and password in `src/StudentResultManagement.java` before running.

## Compile and run
Example for Windows (adjust the Connector/J JAR path to your installation):

```bash
javac -cp ".;mysql-connector-j-9.*/mysql-connector-j-9.*.jar" src/StudentResultManagement.java
java -cp ".;src;mysql-connector-j-9.*/mysql-connector-j-9.*.jar" StudentResultManagement
```

On Linux/macOS, use `:` instead of `;` in the classpath.

## Grade rules used in this demonstration
- 90–100: A+
- 80–89.99: A
- 70–79.99: B
- 60–69.99: C
- 50–59.99: D
- Below 50: F
- Pass requires at least 35 marks in every subject; otherwise the result is FAIL.

These are sample rules for the project. Replace them with the grading policy approved by your institution.

## Report
Open `report/main.tex` in Overleaf and upload the `report` folder contents, or upload the repository ZIP and select `report/main.tex` as the main document. Replace student names, roll numbers, facilitator details, and college details with your actual information.

## Security note
This is an educational prototype. It uses parameterized SQL queries, but production deployments should add authenticated roles, password hashing, input validation, backups, audit logs, and access controls. Do not store real student information in a public repository.
