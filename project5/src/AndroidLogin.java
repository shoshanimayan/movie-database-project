

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

import org.jasypt.util.password.StrongPasswordEncryptor;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class AndroidLogin
 */
@WebServlet("/AndroidLogin")
public class AndroidLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String login = request.getParameter("query");
		if (login == null)
			login = "asdf_asdf";
		
		String loginUser = "mytestuser";
	    String loginPasswd = "catcat123";
	    String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

	    PrintWriter out = response.getWriter();   
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
			// create database connection
			Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			
			String[] info = login.split("_");
	    	String email = info[0];
	    	String password = info[1];
	    	
	    	JsonObject j = new JsonObject();
	    	
	    	String query = "SELECT * from customers where email = ? ";
    		PreparedStatement stmt = connection.prepareStatement(query);
    		stmt.setString(1, email);	
    		
    		ResultSet result = stmt.executeQuery();
    			
    		if (!result.next())
    		{	
    			j.addProperty("msg", "Email does not exist");		
    		}
    		
    		else
    		{
    			result = stmt.executeQuery();
    			boolean valid = false;
    			
        		if (result.next()) {
        			String encrypted = result.getString("password");
        			valid =  new StrongPasswordEncryptor().checkPassword(password, encrypted);
        		}
        		
        		if (!valid) {
        			j.addProperty("msg", "Incorrect password");
        		} 
        		
        		else
        		{
    				j.addProperty("msg", "Success");
        		}
    		}
    		
			result.close();
			stmt.close();
			connection.close();
			response.getWriter().write(j.toString());
			
			return;
	    }
	    
	    catch (Exception e) {
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

}
