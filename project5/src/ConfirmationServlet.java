

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class ConfirmationServlet
 */
@WebServlet("/ConfirmationServlet")
public class ConfirmationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfirmationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, Integer> cart= new HashMap<String,Integer>();
        cart = (HashMap<String, Integer>)request.getSession().getAttribute("cart");
		
		String email = (String)request.getSession().getAttribute("email");
      //  if (email == null || cart==null)
		//    response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");
                
        
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
        out.println("button{cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("tr:hover {background-color: #f2f2f2;}");
        out.println("table {border-collapse: collapse; width: 75%; }");
        out.println("table, td, tr {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println("th {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #E6FFFF;}");
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
         		
    		
    		String query = "SELECT id FROM customers WHERE email = ?";
    		PreparedStatement stmt = connection.prepareStatement(query);
    		stmt.setString(1, email);
    		
    		ResultSet result = stmt.executeQuery();
    		
    		int customer_id = 0;
		    if (result.next()) {
		    	customer_id = Integer.parseInt(result.getString("id"));
		    }
		    
    		String pattern = "yyyy-MM-dd";
    		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    		String date = formatter.format(new Date());	
    		
    		for (Map.Entry<String, Integer> item : cart.entrySet()) {
    		    String movie_id = item.getKey();
    		    Integer quantity = item.getValue();
		   
    		    String update = "INSERT INTO sales VALUES (NULL, ?, ?, ?, ?)";
    		    stmt = connection.prepareStatement(update);
    		    
    		    stmt.setInt(1, customer_id);
    		    stmt.setString(2, movie_id);
    		    stmt.setString(3, date);
    		    stmt.setInt(4, quantity);
    		    
    		    stmt.executeUpdate();
    		}
    	
    		out.println("<body>");
    		out.println("<button onclick=\"window.location.href = \'/project1/MainPage\';\"><h4>Main Page</h4></button>");
    		out.println("<center>");
    		out.println("<h1>Transaction Complete!</h1>");
    		out.println("<br>");
    		out.println("<h2>Order Details</h2>");
    		
    		out.println("<table border>");
    		out.println("<tr>");
    		out.println("<th width=\"10%\">Sale ID</th>");
    		out.println("<th width=\"30%\">Movie</th>");
    		out.println("<th width=\"10%\">Quantity</th>");
    		out.println("</tr>");
    		
    		for (Map.Entry<String, Integer> item : cart.entrySet()) {
    			
    		    String id = item.getKey();
    		    Integer quantity = item.getValue();
    		    
    		    query = "SELECT title FROM movies WHERE id = ?";
    		    stmt = connection.prepareStatement(query);
    		    stmt.setString(1, id);
    		    result = stmt.executeQuery();
    		    
    		    while (result.next())
    		    {
    		    	String title = result.getString("title");
    		    	
    		    	query = "SELECT id FROM sales WHERE customerId = ? AND movieId = ? AND saleDate = ? AND count = ? ORDER BY id DESC LIMIT 1";
    		    	PreparedStatement stmt2 = connection.prepareStatement(query);
    		    	stmt2.setInt(1, customer_id);
    		    	stmt2.setString(2, id);
    		    	stmt2.setString(3, date);
    		    	stmt2.setInt(4, quantity);
    		    	
    		    	ResultSet result2 = stmt2.executeQuery();
    		    	
    		    	int sale_id = 0;
    		    	if (result2.next()) {
    		    		sale_id = Integer.parseInt(result2.getString("id"));
    		    	}
    		    	
    		    	out.println("<tr>");
    		    	out.println("<td width=\"10%\">" + sale_id + "</td>");
        			out.println("<td width=\"30%\">" + title + "</td>");
        			out.println("<td width=\"10%\">" + quantity + "</td>");	
        			out.println("</tr>");
    		    }
    		}
    		
    		cart.clear();
    		    
    		out.println("</center>");
    		out.println("</body>");
    		
    		request.getSession().setAttribute("cart", cart);
    		
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
