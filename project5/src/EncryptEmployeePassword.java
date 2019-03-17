
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class EncryptEmployeePassword {
	/*
     * 
     * Updates the existing moviedb employees table to change the
     * plain text passwords to encrypted passwords.
     * 
     * You should only run this program **once**, because this program uses the
     * existing passwords as real passwords, then replace them. If you run it more
     * than once, it will treat the encrypted passwords as real passwords and
     * generate wrong values.
     * 
     */
    public static void main(String[] args) throws Exception {

       // String loginUser = "mytestuser";
        //String loginPasswd = "catcat123";
        //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        Class.forName("com.mysql.jdbc.Driver").newInstance();
     // create database connection
		 Context initCtx = new InitialContext();

        Context envCtx = (Context) initCtx.lookup("java:comp/env");
   
        DataSource ds = (DataSource) envCtx.lookup("jdbc/write_moviedb");

        Connection connection= ds.getConnection();  
        
		
        // change the employees table password column from VARCHAR(20) to VARCHAR(128)
		String alterQuery = "ALTER TABLE employees MODIFY COLUMN password VARCHAR(128) NOT NULL";
		PreparedStatement stmt = connection.prepareStatement(alterQuery);
		int alterResult = stmt.executeUpdate();
        System.out.println("altering employees table schema completed, " + alterResult + " rows affected");

        // get the email and password for each employee
        String query = "SELECT email, password from employees";
        stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        
        // we use the StrongPasswordEncryptor from jasypt library (Java Simplified Encryption) 
        //  it internally use SHA-256 algorithm and 10,000 iterations to calculate the encrypted password
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        ArrayList<PreparedStatement> updateQueryList = new ArrayList<>();

        System.out.println("encrypting password (this might take a while)");
        while (rs.next()) {
            // get the email and plain text password from current table
            String email = rs.getString("email");
            String password = rs.getString("password");
            
            // encrypt the password using StrongPasswordEncryptor
            String encryptedPassword = passwordEncryptor.encryptPassword(password);

            // generate the update query   
            String updateQuery = "UPDATE employees SET password= ? WHERE email= ? ";
            PreparedStatement stmt2 = connection.prepareStatement(updateQuery);
            stmt2.setString(1, encryptedPassword);
            stmt2.setString(2, email);
            updateQueryList.add(stmt2);
        }
        rs.close();

        // execute the update queries to update the password
        System.out.println("updating password");
        int count = 0;
        for (PreparedStatement st : updateQueryList) {
            int updateResult = st.executeUpdate();
            count += updateResult;
        }
        System.out.println("updating password completed, " + count + " rows affected");

        connection.close();

        System.out.println("finished");

    }

}
