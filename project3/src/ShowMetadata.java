

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project1.helperFunct;

/**
 * Servlet implementation class ShowMetadata
 */
@WebServlet("/ShowMetadata")
public class ShowMetadata extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowMetadata() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = (String)request.getSession().getAttribute("employee_email");
		
		if (email == null)
		    response.sendRedirect("/project1/_dashboard?errormsg=You are not logged in");
						
		// change this to your own mysql username and password
    	String loginUser = "mytestuser";
	    String loginPasswd = "mypassword";
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
        out.println("button {cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("tr:hover {background-color: #f2f2f2;}");
        out.println("table {border-collapse: collapse; width: 35%; }");
        out.println("table, td, tr {border: 2px solid;  padding: 11px; text-align: left; font-family: Arial}");
        out.println("th {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #d0e1e1;}");
        out.println("</style>");
        out.println("</head>");        
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		// declare statement
    		
    		DatabaseMetaData meta = connection.getMetaData();
            ResultSet result = meta.getTables(null, null,"%", null);        

            //set up body
    		out.println("<body>");
    		out.println("<center>"); 
    		out.println("<h1>Fablix Metadata</h1>");

    		 while (result.next())
             {			 
    			 String table_name = result.getString(3);
    			 out.println("<table border>");
    			 out.println("<tr>");
    			 out.println("<th colspan=\"2\">" + table_name + "</th>");
    			 out.println("</tr>");
    			 
    			 out.println("<tr>");
    			 out.println("<th>Attribute</th>");
    			 out.println("<th>Type</th>");
    			 out.println("</tr>");
    			 
    			 /*
    			 String query = "SELECT * from ? LIMIT 1;";
    			 PreparedStatement stmt = connection.prepareStatement(query);
    			 stmt.setString(1, table_name);
    			 ResultSet rs = stmt.executeQuery();
    			 */
    			 
    			 Statement stmt = connection.createStatement();
    			 ResultSet rs = stmt.executeQuery("SELECT * FROM " + table_name + " LIMIT 1");
    			 
    			 ResultSetMetaData rsmd = rs.getMetaData();
    			 int columns = rsmd.getColumnCount();

    			 // Column count starts from 1
    			 for (int i = 1; i <= columns; i++ ) {
    			   String col_name = rsmd.getColumnName(i);
    			   String col_type = rsmd.getColumnTypeName(i);
    			   out.println("<tr>");
    			   out.println("<td>" + col_name + "</td>");
    			   out.println("<td>" + col_type + "</th>");
    			   out.println("</tr>");
    			 }
    			 out.println("</table>");
    			 out.println("<br>");
             	
             }
    		
    		out.println("</center>");
    		out.println("</body>");
    		
    		connection.close();
    			
    		
        } catch (Exception e) {
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
