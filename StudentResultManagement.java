import java.sql.*;
import java.util.Scanner;

/** Educational console application for student marks and grade management. */
public class StudentResultManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password"; // Change this value.

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void createTable(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS student_results (" +
            "roll_no VARCHAR(20) PRIMARY KEY, student_name VARCHAR(100) NOT NULL, " +
            "branch VARCHAR(30) NOT NULL, academic_year VARCHAR(20) NOT NULL, " +
            "subject1 DECIMAL(5,2) NOT NULL, subject2 DECIMAL(5,2) NOT NULL, " +
            "subject3 DECIMAL(5,2) NOT NULL, subject4 DECIMAL(5,2) NOT NULL, " +
            "subject5 DECIMAL(5,2) NOT NULL, total DECIMAL(6,2) NOT NULL, " +
            "percentage DECIMAL(5,2) NOT NULL, grade VARCHAR(2) NOT NULL, " +
            "result_status VARCHAR(10) NOT NULL)";
        try (Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        }
        System.out.println("Student results table is ready.");
    }

    private static String grade(double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        if (percentage >= 50) return "D";
        return "F";
    }

    private static void insertStudent(Connection con, Scanner sc) throws SQLException {
        System.out.print("Roll number: "); String roll = sc.next();
        sc.nextLine();
        System.out.print("Student name: "); String name = sc.nextLine();
        System.out.print("Branch: "); String branch = sc.nextLine();
        System.out.print("Academic year: "); String year = sc.nextLine();
        double[] m = readMarks(sc);
        double total = 0; boolean pass = true;
        for (double mark : m) { total += mark; if (mark < 35) pass = false; }
        double pct = total / m.length;
        String result = pass ? "PASS" : "FAIL";
        String sql = "INSERT INTO student_results " +
            "(roll_no,student_name,branch,academic_year,subject1,subject2,subject3,subject4,subject5,total,percentage,grade,result_status) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, roll); ps.setString(2, name); ps.setString(3, branch); ps.setString(4, year);
            for (int i = 0; i < 5; i++) ps.setDouble(5 + i, m[i]);
            ps.setDouble(10, total); ps.setDouble(11, pct); ps.setString(12, grade(pct)); ps.setString(13, result);
            ps.executeUpdate();
        }
        System.out.println("Student result inserted successfully.");
    }

    private static double[] readMarks(Scanner sc) {
        double[] marks = new double[5];
        for (int i = 0; i < marks.length; i++) {
            while (true) {
                System.out.print("Subject " + (i + 1) + " marks (0-100): ");
                double v = sc.nextDouble();
                if (v >= 0 && v <= 100) { marks[i] = v; break; }
                System.out.println("Enter a mark between 0 and 100.");
            }
        }
        return marks;
    }

    private static void displayAll(Connection con) throws SQLException {
        String sql = "SELECT * FROM student_results ORDER BY roll_no";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-12s %-20s %-8s %-8s %-8s %-8s%n", "Roll No", "Name", "Total", "Percent", "Grade", "Result");
            while (rs.next()) {
                System.out.printf("%-12s %-20s %-8.2f %-8.2f %-8s %-8s%n",
                    rs.getString("roll_no"), rs.getString("student_name"), rs.getDouble("total"),
                    rs.getDouble("percentage"), rs.getString("grade"), rs.getString("result_status"));
            }
        }
    }

    private static void searchStudent(Connection con, Scanner sc) throws SQLException {
        System.out.print("Roll number to search: "); String roll = sc.next();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM student_results WHERE roll_no=?")) {
            ps.setString(1, roll);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Name: " + rs.getString("student_name") + ", Branch: " + rs.getString("branch") +
                        ", Total: " + rs.getDouble("total") + ", Percentage: " + rs.getDouble("percentage") +
                        ", Grade: " + rs.getString("grade") + ", Result: " + rs.getString("result_status"));
                } else System.out.println("No student found for that roll number.");
            }
        }
    }

    private static void updateMarks(Connection con, Scanner sc) throws SQLException {
        System.out.print("Roll number to update: "); String roll = sc.next();
        double[] m = readMarks(sc); double total = 0; boolean pass = true;
        for (double mark : m) { total += mark; if (mark < 35) pass = false; }
        double pct = total / m.length;
        String sql = "UPDATE student_results SET subject1=?,subject2=?,subject3=?,subject4=?,subject5=?,total=?,percentage=?,grade=?,result_status=? WHERE roll_no=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < 5; i++) ps.setDouble(i + 1, m[i]);
            ps.setDouble(6, total); ps.setDouble(7, pct); ps.setString(8, grade(pct));
            ps.setString(9, pass ? "PASS" : "FAIL"); ps.setString(10, roll);
            int count = ps.executeUpdate();
            System.out.println(count > 0 ? "Student marks updated." : "No student found for that roll number.");
        }
    }

    private static void deleteStudent(Connection con, Scanner sc) throws SQLException {
        System.out.print("Roll number to delete: "); String roll = sc.next();
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM student_results WHERE roll_no=?")) {
            ps.setString(1, roll);
            System.out.println(ps.executeUpdate() > 0 ? "Student deleted." : "No student found for that roll number.");
        }
    }

    public static void main(String[] args) {
        try (Connection con = connect(); Scanner sc = new Scanner(System.in)) {
            createTable(con);
            int choice;
            do {
                System.out.println("\n--- Student Results and Grade Management System ---");
                System.out.println("1. Insert student result\n2. Display all results\n3. Search by roll number");
                System.out.println("4. Update marks\n5. Delete student\n6. Exit");
                System.out.print("Enter choice: ");
                while (!sc.hasNextInt()) { System.out.print("Enter a number: "); sc.next(); }
                choice = sc.nextInt();
                try {
                    switch (choice) {
                        case 1: insertStudent(con, sc); break;
                        case 2: displayAll(con); break;
                        case 3: searchStudent(con, sc); break;
                        case 4: updateMarks(con, sc); break;
                        case 5: deleteStudent(con, sc); break;
                        case 6: System.out.println("Exiting application."); break;
                        default: System.out.println("Invalid choice.");
                    }
                } catch (SQLException ex) {
                    System.out.println("Database operation failed: " + ex.getMessage());
                }
            } while (choice != 6);
        } catch (SQLException ex) {
            System.out.println("Could not connect to database. Check MySQL and credentials: " + ex.getMessage());
        }
    }
}
