

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PaymentServlet
 */
@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentServlet() {
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
        
        if (cart.size() <= 0)
		    response.sendRedirect("/project1/ShoppingCart?errormsg=No items to checkout");	
				
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
        out.println("button{cursor: pointer; border: 1px solid black; border-radius: 4px; }");
        out.println("tr:nth-child(even) {background-color: #e2e2e2;}");
        out.println("table {border-collapse: collapse;  width: 75%;  }");
        out.println("table, tr, td {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println("label {display: inline-block; width: 140px; text-align: right;}");
        out.println("</style>");
        out.println("</head>");        
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);	
    		
    		//set up body
    		out.println("<body>");
    		out.println("<button onclick=\"window.location.href = \'/project1/ShoppingCart\';\"><h4>Back to Cart</h4></button>");
    		out.println("<center>"); 
    		if (errormsg != null)
    			out.println(errormsg);
    		
    		out.println("<h1>Payment Information</h1>");
    		out.println("<form id=\"payment_form\" method=\"post\" action=\"/project1/PaymentFilterServlet\">");
    		
    	    out.println("<label><b>First Name of Cardholder</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter first name\" name=\"first_name\" required>");
    	    out.println("<br><br>");
    	    
    	    out.println("<label><b>Last Name of Cardholder</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter last name\" name=\"last_name\" required>");
    	    out.println("<br><br>");
    	    
    	    out.println("<label><b>Card Number</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter card number\" name=\"card_num\" required>");
    	    out.println("<br><br>");
    	    
    	    out.println("<label><b>Expiration Date</b></label>");
    	    out.println("<input type=\"date\" name=\"exp_date\" required>");
    	    out.println("<br><br>");
    	    
    	    out.println("<input type=\"submit\"  value=\"Checkout\">");
    	    out.println("</form>");
    	    
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
