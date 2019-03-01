

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
 * Servlet implementation class AndroidStar
 */
@WebServlet("/AndroidStar")
public class AndroidStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidStar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String srch = request.getParameter("query");
		if (srch==null) {srch="nm0000001";}
		
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

			
			String query =  "SELECT * From stars as s\r\n" + 
					"Left Join(\r\n" + 
					"select name, group_concat(title) as mlist, group_concat(movieId) as movieID from stars_in_movies left join stars on stars_in_movies.starId = stars.id  \r\n" + 
					"left join movies on stars_in_movies.movieId = movies.id  Group by name \r\n" + 
					") sm on s.name=sm.name\r\n" + 
					"where s.id = ? ";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, srch);
			ResultSet resultSet = stmt.executeQuery();
			
			//set up body
			

			JsonArray jsonArray = new JsonArray();

			
			while (resultSet.next()) {
				jsonArray.add(generateJsonObject(resultSet.getString("name"),resultSet.getString("birthYear"),resultSet.getString("mlist"),resultSet.getString("movieID") ));

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
	private static JsonObject generateJsonObject(String name, String year, String mlist, String movieId ) {		
		JsonObject j = new JsonObject();
		j.addProperty("name", name);
		j.addProperty("year", year);
		j.addProperty("mlist", mlist);
		j.addProperty("movieId", movieId);
		

		return j;
	}


}
