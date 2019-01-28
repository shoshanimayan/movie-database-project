

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
 * Servlet implementation class SingleStarServlet
 */
@WebServlet("/SingleStarServlet")
public class SingleStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleStarServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		//response.getWriter().append("Served at: ").append(request.getParameter("moviename"));
		
		String star_to_search = request.getParameter("query");
		
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
        		// prepare query, custom made for this problem
        		String query =  "SELECT * From stars as s\r\n" + 
        				"Join(\r\n" + 
        				"select name, group_concat(title) as mlist, group_concat(movieId) as movieID from stars_in_movies join stars on stars_in_movies.starId = stars.id  \r\n" + 
        				"join movies on stars_in_movies.movieId = movies.id  Group by name \r\n" + 
        				") sm on s.name=sm.name\r\n" + 
        				"where s.id = \""+star_to_search+"\"" ;
        		// execute query, taken from example
        		ResultSet resultSet = statement.executeQuery(query);
        		//set up body
        		out.println("<body>");
        		//out.println("<a>" +star_to_search+"</a>"); //for debugging jdbc cannot read characters with umla
        		out.println("<button onclick=\"window.location.href = \'/project1/MovieServlet\';\"><h4>movie list</h4></button>");
        		out.println("<center>"); // hopefully will make it look nicer 

        		out.println("<table border>");
        		
        		// set up table header
        		out.println("<tr>");
        		out.println("<td>Name</td>");
        		out.println("<td>Year of Birth</td>");
        		out.println("<td>Movies</td>");
        		
        		out.println("</tr>");
        		
        		while (resultSet.next()) {
        			String name = resultSet.getString("name");
        			String year = resultSet.getString("birthYear");
        			String movieList = resultSet.getString("mlist");
     
        			helperFunct help = new helperFunct();

        			
        				out.println("<h1>" + name + "</h1>");
	        			out.println("<tr>");
	        			
	        			out.println("<td>" + name + "</td>");
	        			out.println("<td>" + year + "</td>");
	        			out.println("<td>" + help.lister(movieList,resultSet.getString("movieID"),"/project1/SingleMovieServlet") + "</td>");
	        			
	        			out.println("</tr>");
        			
        		}
        		
        		out.println("</table>");
        		out.println("</center>");
        		out.println("</body>");
        		resultSet.close();
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
		doGet(request, response);
	}

}
