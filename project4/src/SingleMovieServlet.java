

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

import project1.helperFunct;

/**
 * Servlet implementation class SingleMovieServlet
 */
@WebServlet("/SingleMovieServlet")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleMovieServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = (String)request.getSession().getAttribute("email");
        if (email == null)
		    response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");	
		
		String movie_to_search = request.getParameter("query");
		
		Map<String, Integer> cart= new HashMap<String,Integer>();
        cart = (HashMap<String, Integer>)request.getSession().getAttribute("cart");

		// change this to your own mysql username and password
    	String loginUser = "mytestuser";
	    String loginPasswd = "catcat123";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();
        
        String addTo= request.getParameter("cartAdd");
        if(addTo!=null) {
        	if(!cart.containsKey(addTo)) {
        		cart.put(addTo, 1);
        	}
        	else {
        		out.println("first: "+cart.get(addTo));
        		cart.put(addTo,cart.get(addTo)+1);
        		out.println("after: "+cart.get(addTo));
        	}        	
        }
        
        //set up html page
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Fabflix</title>");
        out.println("<style>");
        out.println("table {border-collapse: collapse;  width: 75%;  }");
        out.println("button{cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("table, td, tr {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println("th {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #E6FFFF;}");
        out.println("</style>");
        out.println("</head>");  

        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

    		
    		// prepare query, custom made for this problem
    		String query =  "SELECT * FROM movies as m\r\n" + 
    				"		Left JOIN  ratings as r ON r.movieId = m.id\r\n" + 
    				"       join ( \r\n" + 
    				"       select movieId, title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id \r\n" + 
    				"       join movies on genres_in_movies.movieId = movies.id Group by movies.id\r\n" + 
    				"       ) as gm\r\n" + 
    				"       ON gm.movieId = m.id \r\n" + 
    				"       join (\r\n" + 
    				"       select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id \r\n" + 
    				"       join movies on stars_in_movies.movieId = movies.id Group by movies.id\r\n" + 
    				"       ) as sm\r\n" + 
    				"       ON sm.movieId = m.id\r\n" + 
    				"       where m.id = ? ";
    		
            request.getSession().setAttribute("cart", cart);
            
    		PreparedStatement stmt = connection.prepareStatement(query);
    		stmt.setString(1, movie_to_search);
            
    		ResultSet resultSet = stmt.executeQuery();
    		
    		//set up body
    		out.println("<body>");        
    		out.println("<button onclick=\"window.location.href = \'/project1/MovieServlet\';\"><h4>Movie List</h4></button>");
    		out.println("<button onclick=\"window.location.href = \'/project1/ShoppingCart\';\"><h4>Checkout</h4></button>");
    		out.println("<center>");  
    		out.println("<table border>");
    		
    		// set up table header
    		out.println("<tr>");
    		out.println("<th>Year</th>");
    		out.println("<th>Director</th>");
    		out.println("<th>Genres</th>");
    		out.println("<th>Stars</th>");
    		out.println("<th>Rating</th>");
    		out.println("</tr>");
    		
    		while (resultSet.next()) {
    			String title = resultSet.getString("title");
    			String year = resultSet.getString("year");
    			String director = resultSet.getString("director");
    			String genres = resultSet.getString("genres");
    			String stars = resultSet.getString("stars");
    			String rating = resultSet.getString("rating");
    			helperFunct help = new helperFunct();
    			
				out.println("<h1>" + title + "</h1>");
    			out.println("<tr>");
    			out.println("<td>" + year + "</td>");
    			out.println("<td>" + director + "</td>");
    			out.println("<td>" + genres + "</td>");
    			out.println("<td>" + help.lister(stars,resultSet.getString("starID"), "/project1/SingleStarServlet") + "</td>");
    			out.println("<td>" + rating + "</td>");
    			out.println("<td width=\"10%\"><button onclick=\"window.location.href = \'/project1/SingleMovieServlet?cartAdd="+resultSet.getString("id")+"&query="+movie_to_search+"\';\" >Add to Cart</button></td>");
    			out.println("</tr>");
    		}
    		
    		out.println("</table>");
    		out.println("</center>");
    		out.println("</body>");

    		resultSet.close();
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
