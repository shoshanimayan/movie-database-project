

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class AddStarFilter
 */
@WebServlet("/AddStarFilter")
public class AddStarFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStarFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = (String)request.getSession().getAttribute("employee_email");
		
	//	if (email == null)
		//    response.sendRedirect("/project1/_dashboard?errormsg=You are not logged in");
						
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
    	    CallableStatement callStmt = connection.prepareCall("{call add_star(?, ?)}");

    		String name = request.getParameter("name");
    		String birthString = request.getParameter("birthyear");
    		Integer birth_year = null;
    		
    		if (birthString != "")
    		{
    			birth_year = Integer.parseInt(birthString);
        	    callStmt.setInt(2, birth_year);
    		}
    		
    		else { callStmt.setNull(2, java.sql.Types.INTEGER); }

    	    callStmt.setString(1, name);
    	    callStmt.execute();
    	    
		    response.sendRedirect("/project1/AddStar?msg=SUCCESS: Star added!");
    		
		    callStmt.close();
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
