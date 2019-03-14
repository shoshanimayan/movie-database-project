

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import project1.helperFunct;

/**
 * Servlet implementation class auto
 */
@WebServlet("/auto")
public class auto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public auto() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String srch = request.getParameter("query");
		if (srch==null) {srch="wonder";}
		String[] pieces= srch.split(" ");
	    	String parsedSearch ="";
	    	for(int i=0; i<pieces.length ;i++) {
	    		if(i>0) {parsedSearch+=" ";}
	    		parsedSearch+="+"+pieces[i]+"*";
	    	}	
	    System.out.println(srch);
		 // change this to your own mysql username and password
	//    String loginUser = "mytestuser";
	  //  String loginPasswd = "catcat123";
	    //String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
	    // set response mime type

	    // get the printwriter for writing response
	    PrintWriter out = response.getWriter();
	    //set up html page
	          
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
			// create database connection
	    	// create database connection
   		 Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");


            if (ds == null)
                out.println("ds is null.");

            Connection connection= ds.getConnection();  
			
			String query =  "SELECT * FROM movies as m Left JOIN  ratings as r ON r.movieId = m.id left join (select movieId, title, group_concat(name) as genres from genres_in_movies left join genres on genres_in_movies.genreId = genres.id left join movies on genres_in_movies.movieId = movies.id Group by movies.id ) as gm ON gm.movieId = m.id left join ( select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies left join stars on stars_in_movies.starId = stars.id left join movies on stars_in_movies.movieId = movies.id Group by movies.id ) as sm ON sm.movieId = m.id WHERE MATCH (m.title) AGAINST (? IN BOOLEAN MODE)"        ;		 
			
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, parsedSearch);
			ResultSet resultSet = stmt.executeQuery();
			
			//set up body
			

			JsonArray jsonArray = new JsonArray();

			
			while (resultSet.next()) {
				jsonArray.add(generateJsonObject(resultSet.getString("id"),resultSet.getString("title"),resultSet.getString("director"),resultSet.getString("genres"),resultSet.getString("rating"),resultSet.getString("stars"),resultSet.getString("year") ));

	    		}
	    		
		
			resultSet.close();
			stmt.close();
			connection.close();
			
			JsonObject j = new JsonObject();
			j.add("list", jsonArray);
			response.getWriter().write(j.toString());
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
	private static JsonObject generateJsonObject(String ID, String title, String Director, String Genres, String rating, String stars, String year ) {		
		JsonObject j = new JsonObject();
		j.addProperty("value", title);
		j.addProperty("ID", ID);
		j.addProperty("Dir", Director);
		j.addProperty("genres", Genres);
		j.addProperty("rate", rating);
		j.addProperty("stars", stars);
		j.addProperty("year", year);

		return j;
	}
}
