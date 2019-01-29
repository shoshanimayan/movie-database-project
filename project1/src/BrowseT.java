

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
 * Servlet implementation class BrowseGenre
 */
@WebServlet("/BrowseT")
public class BrowseT extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrowseT() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//session
		 request.getSession().setAttribute("title", null);
	        request.getSession().setAttribute("star", null);
	        request.getSession().setAttribute("director", null);
	        request.getSession().setAttribute("year", null);
	        request.getSession().setAttribute("bGenre", null);
	        request.getSession().setAttribute("bTitle", null);
	        request.getSession().setAttribute("direction", "DESC");
	        request.getSession().setAttribute("sort", "r.rating");
		
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
        out.println("tr:hover {background-color: #e2e2e2;}");
        out.println("table {border-collapse: collapse; width: 75%; }");
        out.println("table, td, tr {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println("</style>");
        out.println("</head>");
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		// prepare query, custom made for this problem
        		//String query =  "SELECT name from genres";
    
        		// execute query , taken from example
        		//ResultSet resultSet = statement.executeQuery(query);
        		//set up body
        		out.println("<body>");
        		out.println("<center>"); // hopefully will make it look nicer 
        		out.println("<h1>Title Letters</h1>");
        		
        		//resultSet.getString("name");
        		char[] alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
for( int i = 0; i<36;i++)  {
        			out.println("<h3><a href = \"/project1/MovieServlet?bTitle="+ alphabet[i]+"\">" + alphabet[i] + "</a></h3>");
        		}
        		
        		out.println("</center>");
        		out.println("</body>");
        		//resultSet.close();
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
