

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
 * Servlet implementation class AutoJS
 */
@WebServlet("/AutoJS")
public class AutoJS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoJS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = (String)request.getSession().getAttribute("email");
	       if (email == null)
			    response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");
		String srch = request.getParameter("query");
		if (srch==null) {srch="wonder bar";}
		String[] pieces= srch.split(" ");
	    	String parsedSearch ="";
	    	for(int i=0; i<pieces.length ;i++) {
	    		if(i>0) {parsedSearch+=" ";}
	    		parsedSearch+="+"+pieces[i]+"*";
	    	}	
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

			
			String query =  "SELECT id, title from movies WHERE MATCH (title) AGAINST (? IN BOOLEAN MODE)";
			
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, parsedSearch);
			ResultSet resultSet = stmt.executeQuery();			

			JsonArray jsonArray = new JsonArray();

			
			while (resultSet.next()) {
				jsonArray.add(generateJsonObject(resultSet.getString("id"),resultSet.getString("title") ));

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
	private static JsonObject generateJsonObject(String ID, String title) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", title);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("ID", ID);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}

}
