

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddMovieFilter
 */
@WebServlet("/AddMovieFilter")
public class AddMovieFilter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovieFilter() {
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
        out.println("</style>");
        out.println("</head>");        
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
    	    CallableStatement callStmt = connection.prepareCall("{call add_movie(?, ?, ?, ?, ?, ?)}");

    		String title = request.getParameter("title");
    		Integer year = Integer.parseInt(request.getParameter("year"));
    		String director = request.getParameter("director");
    		String star = request.getParameter("star");
    		String genre = request.getParameter("genre");
    	    
    		callStmt.setString(1, title);
    	    callStmt.setInt(2, year);
    	    callStmt.setString(3, director);
    	    callStmt.setString(4, star);
    	    callStmt.setString(5, genre);
    	    callStmt.registerOutParameter(6, java.sql.Types.INTEGER);	

    	    callStmt.execute();
			
			String added = callStmt.getString(6);
			if (added.equals("1"))
				response.sendRedirect("/project1/AddMovie?msg=SUCCESS: Movie added!\nStar linked to movie!\nGenre linked to movie!");
			else
				response.sendRedirect("/project1/AddMovie?msg=Movie already exists, no changes made");
    		
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
