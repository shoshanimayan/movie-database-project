

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.util.password.StrongPasswordEncryptor;

import project1.RecaptchaHelper;

/**
 * Servlet implementation class EmployeeLoginFilter
 */
@WebServlet("/EmployeeLoginFilter")
public class EmployeeLoginFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeLoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		// get the printwriter for writing response
        PrintWriter out = response.getWriter();
        
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

        // Verify reCAPTCHA
        try {
            RecaptchaHelper.verify(gRecaptchaResponse);
        } catch (Exception e) {
			response.sendRedirect("/project1/_dashboard?errormsg=Need reCAPTCHA");

        }
   	
		 // change this to your own mysql username and password
        String loginUser = "mytestuser";
	    String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

    		String query = "SELECT * from employees where email = ? ";
    		PreparedStatement stmt = connection.prepareStatement(query);
    		stmt.setString(1, email);
    		
    		ResultSet result = stmt.executeQuery();
    			
    		if (!result.next())
    			response.sendRedirect("/project1/_dashboard?errormsg=Email does not exist");
    				
    		result = stmt.executeQuery();
    		boolean pass = false;
    		if (result.next()) {
    			String encrypted = result.getString("password");
    			pass = new StrongPasswordEncryptor().checkPassword(password, encrypted);
    		}
    		
    		if(!pass) 
    			response.sendRedirect("/project1/_dashboard?errormsg=Incorrect password");
    		
    		else
    		{
		        request.getSession().setAttribute("employee_email", email);
			    response.sendRedirect("/project1/_dashboard");	
    		}
    		
    		result.close();
    		stmt.close();
    		connection.close();
        		
		
        } catch (Exception e) {
    		/*
    		 * After you deploy the WAR file through tomcat manager webpage,
    		 *   there's no console to see the print messages.
    		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
    		 * 
    		 * To view the last n lines (for example, 100 lines) of messages you can use:
    		 *   tail -100 catalina.out
    		 * This can help you debug your program after deploying it on AWS.
    		 */
    		e.printStackTrace();
    		
    		out.println("<body>");
    		out.println("<p>");
    		out.println("Exception in doGet: " + e.getMessage());
    		out.println("</p>");
    		out.print("</body>");
        }
        
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
