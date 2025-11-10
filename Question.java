import java.sql.*;
import java.util.*;
import java.util.Date;

public class Question {
    // Main function, where all the client-sided logic resides
    public static void main(String[] args) throws SQLException {
        int input = 0;
        Scanner user = new Scanner(System.in);
        // We want system to run forever, or at least until it's forcefully shut down by user.
        while (true) {
            // Switch case to dictate the current operation to run (add, delete, etc...)
            switch (input) {
                // Case that handles the displaying of a menu to the user
                case 0:
                    System.out.println("----------------------------------------");
                    System.out.println("Press 1 to display all students");
                    System.out.println("Press 2 to add a student");
                    System.out.println("Press 3 to delete a specific student");
                    System.out.println("Press 4 to update a specific student's email");
                    System.out.println("Press 5 to quit");
                    System.out.println("----------------------------------------");

                    // If user does not input an integer, ask them to input an integer.
                    if (!user.hasNextInt()){
                        System.out.println("Input an integer");
                        user.next();
                        break;
                    }
                    input = user.nextInt();
                    break;

                // Case that handles the displaying of all available users in the table
                case 1:
                    // Run the getAllStudents function, which displays all students
                    System.out.println("----------------------------------------");
                    getAllStudents();
                    input = 6;
                    break;

                // Case that handles the addition of a student to the table
                case 2:

                    // Get new input from user, and store that as a first name variable
                    System.out.println("----------------------------------------");
                    Scanner student = new Scanner(System.in);
                    System.out.println("Enter first name (Input \"q\" or \"Q\" to leave)");
                    String fName = student.nextLine().trim();

                    // If user inputs one of the below, quit the current operation and display menu
                    if (fName.equals("Q") || fName.equals("q")) {
                        input = 0;
                        break;
                    }

                    // Make sure no spaces in the string, and name is at least 2 characters
                    while (fName.contains(" ") || fName.length() < 2){
                        System.out.println("Try again using a proper string (without spaces) or a longer string, (Input \"q\" or \"Q\" to leave)");
                        fName = student.nextLine().trim();
                        if (fName.equals("Q") || fName.equals("q")) {
                            input = 0;
                            break;
                        }
                    }
                    if (input == 0) break;

                    // Same logic as inputting the first name above
                    System.out.println("Enter last name (Input \"q\" or \"Q\" to leave)");
                    String lName = student.nextLine().trim();

                    if (lName.equals("Q") || lName.equals("q")) {
                        input = 0;
                        break;
                    }
                    while (lName.contains(" ") || lName.length() < 2){
                        System.out.println("Try again using a proper string (without spaces) or a longer string, (Input \"q\" or \"Q\" to leave)");
                        lName = student.nextLine().trim();
                        if (lName.equals("Q") || lName.equals("q")) {
                            input = 0;
                            break;
                        }
                    }
                    if (input == 0) break;


                    System.out.println("Enter Email Address (Input \"q\" or \"Q\" to leave)");
                    String email = student.nextLine().trim();
                    // Quit operation and display menu if user enters one of the below
                    if (email.equals("q") || email.equals("Q")) {
                        input = 0;
                        break;
                    }

                    // Email is only valid if it contains an "@" and a "."
                    while (!email.contains("@") || !email.contains(".")){
                        System.out.println("Try again using a proper email (Input \"q\" or \"Q\" to leave)");
                        email = student.nextLine().trim();
                        if (email.equals("q") || email.equals("Q")) {
                            input = 0;
                            break;
                        }
                    }
                    if (input == 0) break;

                    System.out.println("Enter date in one line, in format (year-month-date) with hyphen (Input \"q\" or \"Q\" to leave) | (Input \"null\" if date is unknown)");
                    String date;
                    // Have to initialize a value but the real date will always be the one the user chooses
                    java.sql.Date date2 = java.sql.Date.valueOf("2000-01-01");
                    boolean exit = false;

                    // Loops thru until one of three conditions are met:
                    // 1: User decides to quit at any time by pressing "Q" or "q"
                    // 2: User succesfully writes a correct date in the right format
                    // 3: User enters null, indicating they are unaware of the enrollment date.
                    while (!exit) {
                        date = student.nextLine().trim();
                        if (date.equals("q") || date.equals("Q")) {
                            input = 0;
                            break;
                        }
                        else if (date.equalsIgnoreCase("null")){
                            date2 = null;
                            break;
                        }
                        try {
                            // Date was entered in correct format, so parse it as an SQL date.
                            date2 = java.sql.Date.valueOf(date);
                            exit = true;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Date was entered incorrectly, or in wrong format");
                        }
                    }

                    // Checks if user has decided to quit, if not, then add the new student to the table
                    if (input == 0) break;

                    try {
                        addStudent(fName, lName, email, date2);
                    }
                    // Checks if email is unique (One of the conditions of the email variable)
                    catch (org.postgresql.util.PSQLException e){
                        System.out.println("ERROR: This email already exists, please restart with unique values");
                    }
                    input = 6;
                    break;


                // Case to handle the deletion of a student
                case 3:
                    System.out.println("----------------------------------------");
                    Scanner delete = new Scanner(System.in);
                    System.out.println("Indicate by a number which student you would like to remove, (Input \"q\" or \"Q\" to leave)");

                    // If user enters one of below, quit operation
                    if (delete.hasNext("q") || delete.hasNext("Q")){
                        input = 0;
                        break;
                    }

                    // Ask user to input an integer for studentID, if an integer was not inputted
                    if (!delete.hasNextInt()){
                        System.out.println("Try again by inputting a number");
                        break;
                    }

                    // Store the student id to remove
                    int removed = delete.nextInt();

                    // Remove the student id and go back to menu
                    int result = deleteStudent(removed);

                    // The specified studentID doesn't exist, so make sure user knows no student was deleted
                    if (result == 0){
                        System.out.println("Attention: No data was removed, studentID/student does not exist");
                    }
                    else{
                        System.out.println("Success: The student with a student ID of " + removed + " was succesfully deleted");
                    }

                    input = 6;
                    break;


                // Case to handle the alteration of a student's email
                case 4:
                    System.out.println("----------------------------------------");
                    Scanner update = new Scanner(System.in);
                    System.out.println("Input the studentID of the student's email you want to change (or write \"q\" or \"Q\" to leave)");

                    // If user enters one of below, quit the operation
                    if (update.hasNext("q") || update.hasNext("Q")){
                        input = 0;
                        break;
                    }

                    if (!update.hasNextInt()) {
                        System.out.println("Try again by writing an integer/number");
                        break;
                    }
                    int id = update.nextInt();

                    // Get the new email from the user
                    System.out.println("Input the email to which you want to change (or write \"q\" or \"Q\"to leave)");
                    String newEmail = update.next().trim();
                    // If user enters one of below, quit the operation
                    if (newEmail.equals("q") || newEmail.equals("Q")) {
                        input = 0;
                        break;
                    }

                    // Make sure email has an "@" and an "." symbol
                    while (!newEmail.contains("@") || !newEmail.contains(".")){
                        System.out.println("Try again using a proper email (Input \"q\" or \"q\" to leave)");
                        newEmail = update.next().trim();
                        if (newEmail.equals("q") || newEmail.equals("Q")) {
                            input = 0;
                            break;
                        }
                    }
                    if (input == 0) break;

                    // Update the student's email and go back to menu
                    try {
                        int result2 = updateStudentEmail(id, newEmail);
                        // Notify user that this studentID didn't exist, so email wasn't changed
                        if (result2 == 0){
                            System.out.println("Attention: No data was altered, studentID/student does not exist");
                        }
                        else{
                            System.out.println("Success: The student with a student ID of " + id + " has had their email updated");
                        }
                    }
                    catch (org.postgresql.util.PSQLException e) {
                        System.out.println("Email already exists in the database, try again.");
                    }

                    input = 6;
                    break;

                // If user presses 5, quit the program
                case 5:
                    return;

                // Extra case to reduce code clutter
                case 6:
                default:
                    System.out.println("----------------------------------------");
                    System.out.println("Press 0 to display menu again");
                    System.out.println("Press 5 to quit application");
                    System.out.println("----------------------------------------");
                    // Only accept integer inputs (While choosing menu options)
                    if (!user.hasNextInt()){
                        System.out.println("Input an integer");
                        user.next();
                        break;
                    }
                    input = user.nextInt();
                    break;
            }
        }
    }

    // Simple function that reduces the process of opening a connection
    public static Connection connect(){
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        String user = "postgres";
        String pass = "0000";
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            if (con == null){
                System.out.println("Failed to establish connection");
            }
            return con;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Function that prints all students in the command line
    public static void getAllStudents() throws SQLException{
        Connection connection = connect();
        Statement stmt = connection.createStatement();
        String SQL = "SELECT * FROM students";
        ResultSet rs = stmt.executeQuery(SQL);
        // Get the amount of columns (In case we add more columns, aka., different variables)
        int cols = rs.getMetaData().getColumnCount();

        // Loop thru the columns and print out the column names, on a singular line
        String[] colNames = new String[cols];
        for (int i = 1; i <= cols; i++) {
            colNames[i-1] = rs.getMetaData().getColumnLabel(i);
        }
        // While there is a row available, loop thru all rows, and print all columns
        // Simply, print the whole table
        List<Object[]> allValues = new ArrayList<>();
        Object[] values = new Object[cols];
        while (rs.next()) {
            int col = 1;
            values = new Object[cols];
            while (col <= cols) {
                values[col-1] = rs.getObject(col);
                col++;
            }
            //System.out.println(Arrays.toString(values));
            allValues.add(values);
         }

        // This whole next segment finds the names and emails that are the longest, and notes down their length
        int maxFN = 0, maxLN = 0, maxEM = 0;
        for (int i = 0; i < allValues.size(); i++){
            int newFN = allValues.get(i)[1].toString().length();
            int newLN = allValues.get(i)[2].toString().length();
            int newEM = allValues.get(i)[3].toString().length();
            if (newFN > maxFN) maxFN = newFN;
            if (newLN > maxLN) maxLN = newLN;
            if (newEM > maxEM) maxEM = newEM;
        }

        /* If the longest name extends above the size of the "first_name" label, which is 10
         Then go ahead and format every other variable to have an extra amount of whitespace
         That will fill it to have the same length as the longest name
         Same process for email
         Enrollment_date will always it's max length be the label itself
         */

        maxFN = Math.max(maxFN, 10); maxLN = Math.max(maxLN, 9); maxEM = Math.max(maxEM, 5);

        // Creates a formatted string that will apply the offsets for each names and emails
        String FN = " %-" + maxFN + "s"; String LN = " %-" + maxLN + "s"; String EM = " %-" + maxEM + "s";


        /*
        %-d Means to force it to the left from the first available spot,
        d represents the amount of characters to extend the string to.
        For a name like John, since it's only 4 characters, if d = 10, 6 spots of whitespace will be added
        This will create an even format between the whole table
         */
        String start = "%-10s";

        // The numerical value is arbitrary here, since the whitespace will be added to the end, it will be invisible
        String end = " %-1s";

        // Creates a complete formatted string, in which we pass all our variables
        String offset = start + " |" + FN + " |" + LN + " |" + EM + " |" + end;

        // Print all the labels correctly first
        System.out.printf(offset, colNames[0], colNames[1], colNames[2], colNames[3], colNames[4]);
        System.out.println();
        System.out.println("-".repeat(37 + maxFN + maxLN + maxEM));

        // Print each row in the formatted way we created
        for (int i = 0; i < allValues.size(); i++){
            System.out.printf(offset, allValues.get(i)[0], allValues.get(i)[1], allValues.get(i)[2], allValues.get(i)[3], allValues.get(i)[4]);
            System.out.println();
        }

        // Close all possible connections
        stmt.close();
        rs.close();
        connection.close();
    }

    // Function to add a new student to the table
    public static void addStudent(String fName, String lName, String email, Date enrollmentDate) throws SQLException{
        Connection connection = connect();
        String[] array = {fName, lName, email};
        String SQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?,?,?,?)";

        // Use a prepared statement to allocate all variables to their designated places
        PreparedStatement stmt = connection.prepareStatement(SQL);
        for (int i = 1; i <= 3; i++){
            stmt.setString(i, array[i-1]);
        }
        // Typecast date to SQL date and update the table
        stmt.setDate(4, (java.sql.Date) enrollmentDate);
        stmt.executeUpdate();

        // Close all connections and print the table again
        stmt.close();
        connection.close();
    }

    // Function to delete a student with a specified ID from the table
    public static int deleteStudent(int studentID) throws SQLException{
        Connection connection = connect();
        String SQL = "DELETE FROM students WHERE student_id = ?";
        PreparedStatement stmt = connection.prepareStatement(SQL);
        stmt.setInt(1, studentID);
        int rs = stmt.executeUpdate();
        // Close all possible connections and print table again
        stmt.close();
        connection.close();
        return rs;
    }

    // Function to update a specific student's email
    public static int updateStudentEmail(int student_id, String new_email) throws SQLException{
        Connection connection = connect();
        String SQL = "UPDATE students SET email = ? WHERE student_id = ?";
        PreparedStatement stmt = connection.prepareStatement(SQL);
        stmt.setString(1, new_email);
        stmt.setInt(2, student_id);
        int rs = stmt.executeUpdate();
        // Close all possible connections and print table again
        stmt.close();
        connection.close();
        return rs;
    }
}
