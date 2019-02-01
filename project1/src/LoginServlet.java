

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String errormsg = request.getParameter("errormsg");
		
		 // change this to your own mysql username and password
		 String loginUser = "root";
	        String loginPasswd = "espeon123";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();
        //set up html page
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Fabflix</title>");
        out.println("<style>");
        out.println("button{cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("tr:nth-child(even) {background-color: #e2e2e2;}");
        out.println("table {border-collapse: collapse;  width: 75%;  }");
        out.println("table, tr, td {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println("</style>");
        out.println("</head>");        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		
        		//set up body
        		out.println("<body>");
        		out.println("<center>"); 
        		
        		if (errormsg != null)
        			out.println(errormsg);
        		
        		out.println("<h1>Login to Database</h1>");
        		out.println("<form id=\"login_form\" method=\"post\" action=\"/project1/LoginFilterServlet?filterTo=/project1/MainPage\">");
        	    out.println("<label><b>Email</b></label>");
        	    out.println("<input type=\"text\" placeholder=\"Enter Email\" name=\"email\">");
        	    out.println("<br>");
        	    out.println("<label><b>Password</b></label>");
        	    out.println("<input type=\"password\" placeholder=\"Enter Password\" name=\"password\">");
        	    out.println("<br>");
        	    out.println("<input type=\"submit\"  value=\"Login\">");
        	    out.println("</form>");
        		
        	    
        		out.println("</center>");
        		out.println("</body>");
        		
        		
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
        
        out.println("</html>");
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
