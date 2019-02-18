

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PaymentFilterServlet
 */
@WebServlet("/PaymentFilterServlet")
public class PaymentFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentFilterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Map<String, Integer> cart= new HashMap<String,Integer>();
        cart = (HashMap<String, Integer>)request.getSession().getAttribute("cart");
		
		String email = (String)request.getSession().getAttribute("email");
        if (email == null || cart==null)
		    response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");	
        
		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		String card_num = request.getParameter("card_num");
		String exp_date = request.getParameter("exp_date");
		
		// get the printwriter for writing response
        PrintWriter out = response.getWriter();
   	
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
    			
    		
    		String query = "SELECT * FROM creditcards WHERE firstName = ? AND lastName = ? AND id = ? AND expiration = ?";
    		PreparedStatement stmt = connection.prepareStatement(query);
    		stmt.setString(1, first_name);
    		stmt.setString(2, last_name);
    		stmt.setString(3, card_num);
    		stmt.setString(4, exp_date);
    		
    		ResultSet result = stmt.executeQuery();
			
    		if (!result.next())
    			response.sendRedirect("/project1/PaymentServlet?errormsg=Your information was incorrect");
    		
    		else  
    			response.sendRedirect("/project1/ConfirmationServlet");	
  
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
