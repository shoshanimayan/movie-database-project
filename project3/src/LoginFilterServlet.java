

import java.io.IOException;
import org.jasypt.util.password.StrongPasswordEncryptor;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.util.password.StrongPasswordEncryptor;

import project1.RecaptchaHelper;
import project1.helperFunct;

/**
 * Servlet implementation class LoginFilterServlet
 */
@WebServlet("/LoginFilterServlet")
public class LoginFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginFilterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = request.getParameter("email");
		if(email ==null||email=="") {email=(String)request.getSession().getAttribute("email");}
		if(email ==null||email=="") {response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");}
		String password = request.getParameter("password");
		String forward = request.getParameter("filterTo");
		
		// get the printwriter for writing response
        PrintWriter out = response.getWriter();
        
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

        // Verify reCAPTCHA
        try {
            RecaptchaHelper.verify(gRecaptchaResponse);
        } catch (Exception e) {
			response.sendRedirect("/project1/LoginServlet?errormsg=need recaptcha");

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
    		// declare statement
    		Statement statement = connection.createStatement();
    		
    		// prepare queries, custom made for this problem
    		PreparedStatement query1 = connection.prepareStatement("SELECT * from customers where email = ? ");
    		query1.setString(1, email);	
    		
    		ResultSet result = query1.executeQuery();
    			
    		if (!result.next())
    			response.sendRedirect("/project1/LoginServlet?errormsg=Email does not exist");
    				
    		result = query1.executeQuery();
    		boolean pass = false;
    		while(result.next()) {
    			String encrypt = result.getString("password");
    			pass =  new StrongPasswordEncryptor().checkPassword(password, encrypt);
    		}
    		
    		if(!pass) {
    			response.sendRedirect("/project1/LoginServlet?errormsg=Incorrect password");
    		} 
    		
    		else
    		{
				Map<String, Integer> cart = new HashMap<String, Integer>();
		        request.getSession().setAttribute("email", email);
		        request.getSession().setAttribute("pass", password );
		        request.getSession().setAttribute("cart", cart );
			    response.sendRedirect(forward);	
    		}
    		
    		result.close();
    		statement.close();
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
		doGet(request, response);
	}

}
