

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
 * Servlet implementation class AddMovie
 */
@WebServlet("/AddMovie")
public class AddMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String email = (String)request.getSession().getAttribute("employee_email");
		String msg = request.getParameter("msg");
		
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
        out.println("label {display: inline-block; width: 140px; text-align: right;}");
        out.println("</style>");
        out.println("</head>");        
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
            //set up body
    		out.println("<body>");
    		out.println("<button onclick=\"window.location.href = \'/project1/_dashboard\';\"><h4>Dashboard</h4></button>");
    		out.println("<center>"); 
    		
    		if (msg != null && msg !="")
    			out.println(msg);
    		
    		out.println("<h1>Add a Movie to Database</h1>");
    		out.println("<br>");
    		
    		out.println("<form id=\"add_movie_form\" method=\"get\" action=\"/project1/AddMovieFilter\">");
    		out.println("<label><b>Movie Title</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter title\" name=\"title\" required>");
    	    out.println("<br>");
    	    out.println("<br>");
    	    out.println("<label><b>Year Released</b></label>");
    	    out.println("<input type=\"number\" min=\"0001\" max=\"9999\" placeholder=\"YYYY\" name=\"year\" required>");
    	    out.println("<br>");
    	    out.println("<br>");
    	    out.println("<label><b>Director</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter director's name\" name=\"director\" required>");
    	    out.println("<br>");
    	    out.println("<br>");
    	    out.println("<label><b>Star</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter star's name\" name=\"star\" required>");
    	    out.println("<br>");
    	    out.println("<br>");
    	    out.println("<label><b>Genre</b></label>");
    	    out.println("<input type=\"text\" placeholder=\"Enter genre\" name=\"genre\" required>");
    	    out.println("<br>");
    	    out.println("<br>");
    	    out.println("<br>");
    	    out.println("<input type=\"submit\" value=\"Submit\">");
    	    out.println("</form>");
    		 
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
