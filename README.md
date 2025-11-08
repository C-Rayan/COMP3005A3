# COMP3005A3
Implementation of database management system interaction with application using JDBC

**Setup Instructions:**
1. To run the application, an installation of JDBC for postgreSQL is required (Keep track of the path/location where JDBC was downloaded)
2. Downloading the source code is also required.


**Steps needed to use the application:**
   1. Within the code, there is a function called connect(), which opens a connection to postgres using JDBC
   2. You need to update the user and pass variable to your own username on Postgres (Viewable in pgAdmin)
   3. You need to create a new database (Using pgAdmin), call it Assignment3 for simplicity (if you call it something else, refer to step 4)
   4. Copy the database creation script in pgAdmin using the QueryTool for your newly created database.
   5. You can now move to the next step, which is running the application
   6. If you called the database something else, you need to change the 'Assignment3' portioon of the string URL variable in the connect() function (within the source code), to the new database name. 

**How to run the application:**

*(Recommended Way)* Simply run it from your desired IDE, using the play button

*(Command Line Way (For Windows)):*
1. Open the terminal
2. Go to your source code's directory
3. Use the following command to compile the code (Leave quotation marks in):
   -  javac -classpath "your path to\postgresql-42.7.8.jar" Question.java
4. Use the following command to run the application:
   -  java -classpath "your path to\postgresql-42.7.8.jar" Question.java 

P. S. Any postgres version should be fine, as long as it's compatible with Java 8. (Others could still work, latest Java 8 compatible version is recommended)


**Using the application**:

There is 5 different inputs: 
  - Press 1 to display all the students currently in the database
  - Press 2 to add a student:
      - This method starts by asking for a first and last name, which should not contain any spaces and should be at least 2 characters long
      - It then asks for the email, which has to be unique from any emails currently in the database
      - Finally, asks for the date, leave empty for a NULL date.
  - Press 3 to delete a student using their StudentID
  - Press 4 to update a student's email using their StudentID:
      - New email has to be unique inside of the database
  - Press 5 to quit

