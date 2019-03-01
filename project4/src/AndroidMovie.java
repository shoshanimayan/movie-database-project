

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class AndroidMovie
 */
@WebServlet("/AndroidMovie")
public class AndroidMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String srch = request.getParameter("query");
		if (srch==null) {srch="tt0094859";}
		
		 // change this to your own mysql username and password
		String loginUser = "root";
	    String loginPasswd = "espeon123";
	    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
	    // set response mime type

	    // get the printwriter for writing response
	    PrintWriter out = response.getWriter();
	    //set up html page
	          
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
			// create database connection
			Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

			
			String query =  "SELECT * FROM movies as m\r\n" + 
    				"		Left JOIN  ratings as r ON r.movieId = m.id\r\n" + 
    				"       left join ( \r\n" + 
    				"       select movieId, title, group_concat(name) as genres from genres_in_movies left join genres on genres_in_movies.genreId = genres.id \r\n" + 
    				"       left join movies on genres_in_movies.movieId = movies.id Group by movies.id\r\n" + 
    				"       ) as gm\r\n" + 
    				"       ON gm.movieId = m.id \r\n" + 
    				"       left join (\r\n" + 
    				"       select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies left join stars on stars_in_movies.starId = stars.id \r\n" + 
    				"       left join movies on stars_in_movies.movieId = movies.id Group by movies.id\r\n" + 
    				"       ) as sm\r\n" + 
    				"       ON sm.movieId = m.id\r\n" + 
    				"       where m.id = ? ";		
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, srch);
			ResultSet resultSet = stmt.executeQuery();
			
			//set up body
			

			JsonArray jsonArray = new JsonArray();

			
			while (resultSet.next()) {
				jsonArray.add(generateJsonObject(resultSet.getString("title"),resultSet.getString("director"),resultSet.getString("genres"),resultSet.getString("rating"),resultSet.getString("stars"),resultSet.getString("starID"),resultSet.getString("year") ));

	    		}
	    		
		
			resultSet.close();
			stmt.close();
			connection.close();
			response.getWriter().write(jsonArray.toString());
			return;
	    		
		
	    } catch (Exception e) {
			response.sendError(500, e.getMessage());
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
	private static JsonObject generateJsonObject(String title, String Director, String Genres, String rating, String stars, String starIds, String year ) {		
		JsonObject j = new JsonObject();
		j.addProperty("title", title);
		j.addProperty("Dir", Director);
		j.addProperty("genres", Genres);
		j.addProperty("rate", rating);
		j.addProperty("stars", stars);
		j.addProperty("starId", starIds);
		j.addProperty("year", year);

		return j;
	}

}
