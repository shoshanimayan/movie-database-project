

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class _dashboard
 */
@WebServlet("/_dashboard")
public class _dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public _dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean logged_in;
		String errormsg = request.getParameter("errormsg");
		String email = (String)request.getSession().getAttribute("employee_email");
		
		if (email == null || errormsg != null)
			logged_in = false;
		
		else
			logged_in = true;
				
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
        out.println("table {border-collapse: collapse;  width: 25%; }");
        out.println("button{cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("table, td, tr {border: 2px solid;  padding: 14px; text-align: center; font-family: Arial}");
        out.println("th {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #85adad;}");
        out.println("label {display: inline-block; width: 140px; text-align: right;}");
        out.println("</style>");
        out.println("</head>");        
        
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
    		//set up body
    		out.println("<body>");
    		out.println("<center>"); 
    		
    		if (!logged_in)
    		{    		
    			if (errormsg != null)
    				out.println(errormsg);
    			
	    		out.println("<script src='https://www.google.com/recaptcha/api.js'></script>");
	    		out.println("<h1>Employee Login</h1>");
	    		out.println("<br>");
	    		out.println("<form id=\"employee_login_form\" method=\"post\" action=\"/project1/EmployeeLoginFilter\">");
	    	    out.println("<label><b>Email</b></label>");
	    	    out.println("<input type=\"text\" placeholder=\"Enter Email\" name=\"email\" required>");
	    	    out.println("<br>");
	    	    out.println("<label><b>Password</b></label>");
	    	    out.println("<input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required>");
	    	    out.println("<br>");
	    	    out.println("<br>");
	    	    out.println("<br>");
	    	    out.println("<div class=\"g-recaptcha\" data-sitekey=\"6Le2P5AUAAAAANWO0tg7PIKQ6ms8WQd6IgYxProo\"></div>");
	    	    out.println("<br>");
	    	    out.println("<input type=\"submit\"  value=\"Login\">");
	    	    out.println("</form>");   	   
    		}
    		
    		else
    		{
	    		out.println("<h1>Employee Dashboard</h1>");
	    		out.println("<br>");
	    		out.println("<table border>");
	    		out.println("<tr>");
	    		out.println("<th>Actions</th>");
	    		out.println("</tr>");
	    		
	    		out.println("<tr>");
	    		out.println("<td><button onclick=\"window.location.href = \'/project1/AddMovie\';\"><h4>Add a Movie</h4></button></td>");
	    		out.println("</tr>");
	    		
	    		out.println("<tr>");
	    		out.println("<td><button onclick=\"window.location.href = \'/project1/AddStar\';\"><h4>Add a Star</h4></button></td>");
	    		out.println("</tr>");
	    		
	    		out.println("<tr>");
	    		out.println("<td><button onclick=\"window.location.href = \'/project1/ShowMetadata\';\"><h4>Show Metadata</h4></button></td>");
	    		out.println("</tr>");
	    		
	    		out.println("</table>");   		
    		}
    		
    		out.println("</center>");
    		out.println("</body>");
    		
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
