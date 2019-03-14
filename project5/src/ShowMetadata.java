

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
		
//		if (email == null)
	//	    response.sendRedirect("/project1/_dashboard?errormsg=You are not logged in");
						
		// change this to your own mysql username and password
    	String loginUser = "mytestuser";
	    String loginPasswd = "catcat123";
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
        out.println("th {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #85adad;}");
        out.println(".secondaryHeader {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #b3cccc;}");
        out.println("</style>");
        out.println("</head>");        
        
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
    		
    		DatabaseMetaData meta = connection.getMetaData();
            ResultSet result = meta.getTables(null, null,"%", null);        

            //set up body
    		out.println("<body>");
    		out.println("<button onclick=\"window.location.href = \'/project1/_dashboard\';\"><h4>Dashboard</h4></button>");
    		out.println("<center>"); 
    		out.println("<h1>Fablix Metadata</h1>");

    		 while (result.next())
             {			 
    			 String table_name = result.getString(3);
    			 out.println("<table border>");
    			 out.println("<tr>");
    			 out.println("<th colspan=\"4\">" + table_name + "</th>");
    			 out.println("</tr>");
    			 
    			 out.println("<tr>");
    			 out.println("<th class=\"secondaryHeader\">Attribute</th>");
    			 out.println("<th class=\"secondaryHeader\">Type</th>");
    			 out.println("<th class=\"secondaryHeader\">Null</th>");
    			 out.println("<th class=\"secondaryHeader\">Key</th>");
    			 out.println("</tr>");
    		
    			 
    			 String query = String.format("desc %s", table_name);
    			 PreparedStatement stmt = connection.prepareStatement(query);
    			 ResultSet rs = stmt.executeQuery();
    			     			 
    			 while (rs.next())
    			 {
    				 String col = rs.getString("Field");
    				 String type = rs.getString("Type");
    				 String null_check = rs.getString("Null");
    				 String key = rs.getString("Key");
    				 
    				 out.println("<tr>");
      			   	 out.println("<td>" + col + "</td>");
      			   	 out.println("<td>" + type + "</th>");
      			   	 out.println("<td>" + null_check + "</th>");
      			   	 out.println("<td>" + key + "</th>");
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
