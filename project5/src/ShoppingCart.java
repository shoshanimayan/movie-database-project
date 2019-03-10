

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class ShoppingCart
 */
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCart() {
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
        if (email == null || cart==null)
		    response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");	
        
        String errormsg = request.getParameter("errormsg");
        
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
        out.println(".checkoutB {cursor: pointer; border: 1px solid black; border-radius: 4px; background-color: #85E085;  font-family: Arial Black; }");
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

    		
    		Queue<String> remove = new LinkedList<>();
            if (cart!=null) {
    	        for (String i: cart.keySet()) {
    	        	if (request.getParameter(i)!=null) {
    	        		//out.println(i);
    	        		Integer value = Integer.parseInt(request.getParameter(i));
    	        		
    	        		if (value>0)
    	        			cart.put(i, value);
    	        		else if (value == 0)
    	        			remove.add(i);
    	        		else
    	        		    response.sendRedirect("/project1/ShoppingCart?errormsg=Cannot enter a negative value");	
    	        	}
    	        	
    	        }
            }
            
            for (String i: remove) {
            	cart.remove(i);
            }
            
            request.getSession().setAttribute("cart", cart);
    	
    		out.println("<body>");	
    		out.println("<button onclick=\"window.location.href = \'/project1/MovieServlet\';\"><h4>Movie List</h4></button>");
    		out.println("<center>");
    		if (errormsg != null)
    			out.println(errormsg);
    		
    		out.println("<h1>Your Shopping Cart</h1>");
    		out.println("<table border>");
    		out.println("<tr>");
    		out.println("<th width=\"30%\">Movie</th>");
    		out.println("<th width=\"10%\">Quantity</th>");
    		out.println("<th width=\"10%\">Set Quantity</th>");
    		out.println("<th width=\"10%\">Delete</th>");    		
    		out.println("</tr>");
    		    		
    		for (Map.Entry<String, Integer> item : cart.entrySet()) {
    		    String id = item.getKey();
    		    Integer quantity = item.getValue();
    		    
    		    String query = "SELECT title FROM movies WHERE id= ? ";
    		    
        		PreparedStatement stmt = connection.prepareStatement(query);
        		stmt.setString(1, id);
             
        		ResultSet result = stmt.executeQuery(); 
    		    
    		    while (result.next())
    		    {
    		    	String title = result.getString("title");
    		    	out.println("<tr>");
        			out.println("<td width=\"30%\">" + title + "</td>");
        			out.println("<td width=\"10%\">" + quantity + "</td>");			
        			out.println("<td width=\"10%\">"+
        			"<form action = \'/project1/ShoppingCart\' method =\'get\'>"+	    		
        				    		"   <input type=\"text\"  name=\""+id+"\" value = \""+quantity+"\" size=\"4\" >" + 
        				    		"    <button type=\"submit\">Submit</button>" + "</form>"
        					+  "</td>");
        			out.println("<td width=\"10%\"><button onclick=\"window.location.href = \'/project1/ShoppingCart?"+id+"="+"0"+"\';\" >Delete</button></td>");
        			out.println("</tr>");
        			
    		    }
    		}
    		
    		out.println("</table>");
    		out.println("<br>");
    		out.println("<br>");
    		out.println("<button class=\"checkoutB\" onclick=\"window.location.href = \'/project1/PaymentServlet\';\"><h4>Proceed to Checkout</h4></button>");
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
