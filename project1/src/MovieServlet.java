

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import project1.helperFunct;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // change this to your own mysql username and password
		String loginUser = "root";
        String loginPasswd = "espeon123";
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
        out.println("tr:hover {background-color: #e2e2e2;}");
        out.println("table {border-collapse: collapse; width: 75%; }");
        out.println("table, td, tr {border: 2px solid;  padding: 14px; text-align: left; font-family: Arial}");
        out.println("</style>");
        out.println("</head>");
        
        //declare data
        String sortBy=null;
        String direction=null;
        String genreBrowse = null;
        String titleBrowse = null;
        String yearSearch = null;
        String titleSearch=null;
        String directorSearch=null;
        String starSearch=null;
        
        
        /// get data from url or session
        sortBy = request.getParameter("sort");
        if(sortBy==null||sortBy=="") {sortBy=(String)request.getSession().getAttribute("sort");}
        if(sortBy==null||sortBy=="") {sortBy="r.rating";}
        
        direction = request.getParameter("direction");
        if(direction==null||direction=="") {direction = (String)request.getSession().getAttribute("direction");}
        if(direction==null||direction=="") {direction="DESC";}
        
        genreBrowse = request.getParameter("bGenre");
        if(genreBrowse=="" || genreBrowse==null) {genreBrowse = (String)request.getSession().getAttribute("bGenre");}
        	
        titleBrowse = request.getParameter("bTitle");
        if(titleBrowse=="" || titleBrowse==null) {titleBrowse = (String)request.getSession().getAttribute("bTitle");}
        
        yearSearch = request.getParameter("year");
        if(yearSearch=="" || yearSearch==null) {yearSearch = (String)request.getSession().getAttribute("year");}
            
        titleSearch = request.getParameter("title"); 
        if(titleSearch=="" || titleSearch==null) {titleSearch = (String)request.getSession().getAttribute("title");}

        directorSearch = request.getParameter("director");
        if(directorSearch=="" || directorSearch==null) {directorSearch = (String)request.getSession().getAttribute("director");}

        starSearch = request.getParameter("star");
        if(starSearch=="" || starSearch==null) {starSearch = (String)request.getSession().getAttribute("star");}
        
        int pCount= 20;
        Integer currentPage;
        currentPage= (Integer)request.getSession().getAttribute("currentPage");
        if(currentPage==null||currentPage<0) {currentPage=0;}
        if("next".equals(request.getParameter("pageMsg"))) {currentPage+=pCount;}
        if("prev".equals(request.getParameter("pageMsg"))) {
        	if(currentPage-pCount>0)
        		currentPage-=pCount;
        	else
        		currentPage=0;
        		
        }
    	//out.println(currentPage);
    	



        ///set data to session
        request.getSession().setAttribute("title", titleSearch);
        request.getSession().setAttribute("star", starSearch);
        request.getSession().setAttribute("director", directorSearch);
        request.getSession().setAttribute("year", yearSearch);
        request.getSession().setAttribute("bGenre", genreBrowse);
        request.getSession().setAttribute("bTitle", titleBrowse);
        request.getSession().setAttribute("direction", direction);
        request.getSession().setAttribute("sort", sortBy);
        request.getSession().setAttribute("pCount", pCount);
        request.getSession().setAttribute("currentPage", currentPage);


        
        
        try {
        		Class.forName("com.mysql.jdbc.Driver").newInstance();
        		// create database connection
        		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        		// declare statement
        		Statement statement = connection.createStatement();
        		// prepare query, custom made for this problem
        		
        		String query;
        		
        		if (genreBrowse != null) {
        			query = "	SELECT * FROM movies as m\r\n" + 
	        				"	JOIN  ratings as r ON r.movieId = m.id\r\n" + 
	        				"   join (\r\n" + 
	        				"   select title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id \r\n" + 
	        				"   join movies on genres_in_movies.movieId = movies.id Group by title HAVING FIND_IN_SET(" +"\"" + genreBrowse + "\", genres) > 0 " + 
	        				"   ) as gm\r\n" + 
	        				"   ON gm.title = m.title\r\n" + 						
	        				"   join ( \r\n" + 
	        				"   select title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id \r\n" + 
	        				"   join movies on stars_in_movies.movieId = movies.id Group by title\r\n" + 
	        				"   ) as sm\r\n" + 
	        				"   ON sm.title = m.title\r\n" + 
	        				"   ORDER BY r.rating desc\r\n" + 
	        				"   limit 20";
        		}
        		
        		else if (titleBrowse != null) {
        			query = "	SELECT * FROM movies as m\r\n" + 
            				"   JOIN  ratings as r ON r.movieId = m.id\r\n" + 
            				"   join (\r\n" + 
            				"   select title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id \r\n" + 
            				"   join movies on genres_in_movies.movieId = movies.id Group by title \r\n" + 
            				"   ) as gm\r\n" + 
            				"   ON gm.title = m.title\r\n" + 						
            				"   join ( \r\n" + 
            				"   select title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id \r\n" + 
            				"   join movies on stars_in_movies.movieId = movies.id Group by title\r\n" + 
            				"   ) as sm\r\n" + 
            				"   ON sm.title = m.title\r\n" + 
            				"   WHERE SUBSTRING(m.title, 1, 1) =" + "\"" + titleBrowse + "\" \r\n" + 
            				"   ORDER BY r.rating desc\r\n" + 
            				"   limit 20";
        		}
        		else {
        		
        			query = "	SELECT * FROM movies as m\r\n" + 
            				"   JOIN  ratings as r ON r.movieId = m.id\r\n" + 
            				"   join (\r\n" + 
            				"   select title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id \r\n" + 
            				"   join movies on genres_in_movies.movieId = movies.id Group by title \r\n" + 
            				"   ) as gm\r\n" + 
            				"   ON gm.title = m.title\r\n" + 						
            				"   join ( \r\n" + 
            				"   select title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id \r\n" + 
            				"   join movies on stars_in_movies.movieId = movies.id Group by title\r\n" + 
            				"   ) as sm\r\n" +
            				"   ON sm.title = m.title\r\n" ;
        			 String where="WHERE ";
        			 if (titleSearch!="" && titleSearch!=null) {where += "m.title LIKE \'%" + titleSearch + "%\'\r\n" ;}
        			 if(yearSearch!="" && yearSearch!=null) {
        				 if(where!="WHERE ") {where+="AND ";}
        				 where += "m.year =" + yearSearch + "\r\n";
        			 }
        			 if(directorSearch!="" && directorSearch != null) {
        				 if(where!="WHERE " ) {where+="AND ";}
        				 where +=  "m.director LIKE \'%" + directorSearch + "%\'\r\n";
        			 }
        			 if(starSearch!="" && starSearch!=null) {
        				 if(where!="WHERE ") {where+="AND ";}
        				 where +=  "stars LIKE \'%" + starSearch + "%\'\r\n";
        			 }
            			if(where!="WHERE ") {query+=where;}
            			
            	//		query+=	"WHERE m.year =" + yearSearch + "\r\n";
            			
            			int Qsize = 0;
                		ResultSet SizeQ = statement.executeQuery(query);
                		if ( SizeQ!= null) 
                		{
                		  SizeQ.beforeFirst();
                		  SizeQ.last();
                		  Qsize = SizeQ.getRow();
                		}
                		if(currentPage>=Qsize) {currentPage=Qsize-pCount;}
            			query+=		"   ORDER BY "+ sortBy+" "+ direction+"\r\n" + 
            				"   limit "+currentPage+", "+pCount;
        		}
    
        		// execute query , taken from example
        		ResultSet resultSet = statement.executeQuery(query);
        		//set up body
        		out.println("<body>");
        		out.print("<form  method=\"get\" >Sort by: <button name=\"sort\" type=\"submit\" value=\"r.rating\">Rating</button><button name=\"sort\" type=\"submit\" value=\"m.title\">Title</button><br>In order: <button name=\"direction\" type=\"submit\" value=\"ASC\">Ascend</button><button name=\"direction\" type=\"submit\" value=\"DESC\">Descend</button></form>");

        		out.println("<center>"); // hopefully will make it look nicer 
        		out.println("<h1>Movies</h1>");
        		
        		out.println("<table border>");
        		
        		// set up table header
        		out.println("<tr>");
        		out.println("<td>Title</td>");
        		out.println("<td>Year</td>");
        		out.println("<td>Director</td>");
        		out.println("<td>Genres</td>");
        		out.println("<td>Stars</td>");
        		out.println("<td>Rating</td>");
        		out.println("</tr>");
        		
        		while (resultSet.next()) {
        			
        			String title = resultSet.getString("title");
        			String year = resultSet.getString("year");
        			String director = resultSet.getString("director");
        			String genres = resultSet.getString("genres");
        			String stars = resultSet.getString("stars");
        			String rating = resultSet.getString("rating");
        			
        			helperFunct help = new helperFunct();
        			
        			out.println("<tr>");
        			out.println("<td><a href = \"/project1/SingleMovieServlet?query="+ resultSet.getString("id")+"\">" + title + "</a></td>");
        			out.println("<td>" + year + "</td>");
        			out.println("<td>" + director + "</td>");
        			out.println("<td>" + genres + "</td>");
        			out.println("<td>" + help.lister(stars, resultSet.getString("starID"), "/project1/SingleStarServlet") + "</td>");
        			out.println("<td>" + rating + "</td>");
        			out.println("</tr>");
        		}
        		
        		out.println("</table>");
        		out.println("<form action=\"/project1/MovieServlet\" method=\"get\"><button name = \"pageMsg\" value=\"prev\">prev</button>"+"<button name = \"pageMsg\" value=\"next\">next</button>"+"</form>");
        		out.println("</center>");
        		out.println("</body>");
        		resultSet.close();
        		statement.close();
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
		doGet(request, response);
	}

}
