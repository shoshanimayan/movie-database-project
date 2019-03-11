

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class MainPage
 */
//@WebServlet(name = "MainPage", urlPatterns = "/MainPage")

@WebServlet("/MainPage")
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
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
		
		String src = request.getParameter("src");
		if (src==null) {src="title";}
		
		// change this to your own mysql username and password
		//String loginUser = "mytestuser";
	   // String loginPasswd = "catcat123";
       // String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
        // set response mime type
        response.setContentType("text/html"); 

        // get the printwriter for writing response
        PrintWriter out = response.getWriter();
        //set up html page
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Fabflix</title>");
        out.println("<style>");
        out.println("button{cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("tr:nth-child(even) {background-color: #e2e2e2;}");
        out.println("table {border-collapse: collapse;  width: 75%;  }");
        out.println("table, tr, td {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println(".autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }\r\n" + 
        		".autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }\r\n" + 
        		".autocomplete-selected { background: #f2f2f2; }\r\n" + 
        		".autocomplete-group { padding: 2px 5px; }\r\n" + 
        		".autocomplete-group strong { display: block; border-bottom: 1px solid #000; }");
        out.println("</style>");
        out.println("</head>");        
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		 Context initCtx = new InitialContext();

             Context envCtx = (Context) initCtx.lookup("java:comp/env");
             if (envCtx == null)
                 out.println("envCtx is NULL");

             DataSource ds = (DataSource) envCtx.lookup("jdbc/moviedb");


             if (ds == null)
                 out.println("ds is null.");

             Connection connection= ds.getConnection();  
    		
    		//set up body
    		out.println("<head>");
    		out.println(" <!-- Using jQuery -->\r\n" + 
    				"    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>\r\n" + 
    				"\r\n" + 
    				"    <!-- include jquery autocomplete JS  -->\r\n" + 
    				"    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js\"></script>\r\n" + 
    				"\r\n" + 
    				"    <!-- Bootstrap for CSS -->\r\n" + 
    				"    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\"></script>");
    		out.println("</head>");
    		out.println("<body>");
    		out.println("<button onclick=\"window.location.href = \'/project1/ShoppingCart\';\"><h4>Checkout</h4></button>");
    		out.println("<center>"); 
    		out.println("<h1>Welcome to Fablix</h1>");
    		out.println("<h3>Full Text Search</h3>");
    		out.println("<form action = \'/project1/MovieServlet?\' method =\'get\'>" +
    					"<input type=\"text\"  name=\"fulltextSearch\" id=\"autocomplete\" class=\"autocomplete-searchbox form-control\"  ><br><br>" +
    					"<input id=\"msg\" name=\"msg\" type=\"hidden\" value=\"clean\">" +
    				    " <button type=\"submit\">Search</button>" +"</form>");
    		
    		out.println("<br>");
    		out.println( "<script src=\"autoCom.js\">console.log(\"Hello world!\");</script>");
    			
    		out.println("<h3>Browse</h3>");
    		out.println("<form action=\"/project1/browse\" method=\"get\"><button>Browse</button></form>");
    		out.println("<h3>Search</h3>");
    		out.println("<form action=\"/project1/Search\" method=\"get\"><button>Search</button></form>");
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
