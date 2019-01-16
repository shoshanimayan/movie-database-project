

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

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
        out.println("<head><title>Fabflix</title></head>");
        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		// prepare query, custome made for this problem
        		String query =  "SELECT * FROM movies as m\r\n" + 
        				"JOIN  ratings as r ON r.movieId = m.id\r\n" + 
        				"\r\n" + 
        				"join (\r\n" + 
        				"select title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id \r\n" + 
        				"join movies on genres_in_movies.movieId = movies.id Group by title\r\n" + 
        				") as gm\r\n" + 
        				"ON gm.title = m.title\r\n" + 
        				"\r\n" + 
        				"join (\r\n" + 
        				"select title, group_concat(name) as stars from stars_in_movies join stars on stars_in_movies.starId = stars.id \r\n" + 
        				"join movies on stars_in_movies.movieId = movies.id Group by title\r\n" + 
        				") as sm\r\n" + 
        				"ON sm.title = m.title\r\n" + 
        				"\r\n" + 
        				"ORDER BY r.rating desc\r\n" + 
        				"limit 20\r\n" + 
        				"" ;
        		// execute query , taken from example
        		ResultSet resultSet = statement.executeQuery(query);
        		//set up body
        		out.println("<body>");
        		out.println("<center>"); // hopefully will make it look nicer 
        		out.println("<h1>Movie Database</h1>");
        		
        		out.println("<table border>");
        		
        		// set up table header
        		out.println("<tr>");
        		out.println("<td>title</td>");
        		out.println("<td>year</td>");
        		out.println("<td>director</td>");
        		out.println("<td>genres</td>");
        		out.println("<td>stars</td>");
        		out.println("<td>rating</td>");
        		out.println("</tr>");
        		
        		while (resultSet.next()) {
        			String title = resultSet.getString("title");
        			String year = resultSet.getString("year");
        			String director = resultSet.getString("director");
        			String genres = resultSet.getString("genres");
        			String stars = resultSet.getString("stars");
        			String rating = resultSet.getString("rating");
        			
        			out.println("<tr>");
        			out.println("<td>" + title + "</td>");
        			out.println("<td>" + year + "</td>");
        			out.println("<td>" + director + "</td>");
        			out.println("<td>" + genres + "</td>");
        			out.println("<td>" + stars + "</td>");
        			out.println("<td>" + rating + "</td>");
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
