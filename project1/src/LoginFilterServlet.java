

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		if(email ==null||email=="") {response.sendRedirect("/project1/LoginServlet?errormsg=you were not logged in");}
		String password = request.getParameter("password");
		String forward = request.getParameter("filterTo");
		
		
		
		// get the printwriter for writing response
        PrintWriter out = response.getWriter();
   	
		 // change this to your own mysql username and password
		String loginUser = "root";
        String loginPasswd = "espeon123";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 
         
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		
        		// prepare query, custom made for this problem
        		String query1 = " SELECT email from customers where email =\"" + email + "\"";
        		String query2 = " SELECT email from customers where email =\"" + email + "\" and password =\"" + password + "\"";

        		
        		// execute query, taken from example
        		ResultSet result = statement.executeQuery(query1);
        		
        		if (!result.next())
        		
        			response.sendRedirect("/project1/LoginServlet?errormsg=Email does not exist");
        			
        		else {
        		result = statement.executeQuery(query2);
        		if (!result.next())
        			response.sendRedirect("/project1/LoginServlet?errormsg=Password is incorrect");
        		
        		else {
        	        request.getSession().setAttribute("email", email);
        		    response.sendRedirect(forward);	
        		}
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
