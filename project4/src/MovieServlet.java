

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

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
		
		Map<String, Integer> cart= new HashMap<String,Integer>();
        cart = (HashMap<String, Integer>)request.getSession().getAttribute("cart");
		
		String email = (String)request.getSession().getAttribute("email");
        if (email == null || cart==null)
		    response.sendRedirect("/project1/LoginServlet?errormsg=You are not logged in");	
        
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
        out.println(".form1 {position: absolute; top: 10px; right: 10px; }");
        out.println(".form2 {position: absolute; top: 40px; right: 10px; }");
        out.println("tr:hover {background-color: #f2f2f2;}");
        out.println("table {border-collapse: collapse; width: 75%; }");
        out.println("table, td, tr {border: 2px solid;  padding: 11px; text-align: left; font-family: Arial}");
        out.println("th {border: 2px solid;  padding: 11px; text-align: center; font-family: Arial; background-color: #E6FFFF;}");
        out.println("</style>");
        out.println("</head>");
        
        //declare data
        String fulltextSearch=null;
        String sortBy=null;
        String direction=null;
        String genreBrowse = null;
        String titleBrowse = null;
        String yearSearch = null;
        String titleSearch=null;
        String directorSearch=null;
        String starSearch=null;
        
        if("clean".equals(request.getParameter("msg"))) {
        	request.getSession().removeAttribute("title");
    		request.getSession().removeAttribute("star");
    		request.getSession().removeAttribute("year");
    		request.getSession().removeAttribute("director");
    		request.getSession().removeAttribute("bTitle");
    		request.getSession().removeAttribute("bGenre");
    		request.getSession().removeAttribute("direction");
    		request.getSession().removeAttribute("sort");
    		request.getSession().removeAttribute("currentPage");
    		request.getSession().removeAttribute("pCount");
    		request.getSession().removeAttribute("fulltextSearch");
        }
        
        /// get data from url or session
        String parsedSearch="";
        fulltextSearch = request.getParameter("fulltextSearch");
        if(fulltextSearch==null||fulltextSearch=="") {
        	fulltextSearch=(String)request.getSession().getAttribute("fulltextSearch");
        }
        if(fulltextSearch!=null) {
	        String[] pieces= fulltextSearch.split(" ");
	    	parsedSearch ="";
	    	for(int i=0; i<pieces.length ;i++) {
	    		if(i>0) {parsedSearch+=" ";}
	    		parsedSearch+="+"+pieces[i]+"*";
	    	}
        }
        out.println(parsedSearch);

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
        
        Integer pCount = 0;
        String count = request.getParameter("pCount");
        if(count==null||count=="") { 
        	pCount =(Integer)request.getSession().getAttribute("pCount");
            if(pCount==null || pCount <= 0) {pCount=20;}
        	}
        else {
        	pCount = Integer.parseInt(count);
        }  
        
        
        Integer currentPage = (Integer)request.getSession().getAttribute("currentPage");
        if(currentPage==null || currentPage < 0) {currentPage=0;}
   
        if("next".equals(request.getParameter("pageMsg"))) {currentPage+=pCount;}
        
        if("prev".equals(request.getParameter("pageMsg"))) {
        	if(currentPage-pCount<0)
        		currentPage=0;
        	else {
        		currentPage-=pCount;
        	}	
        }
        
        String addTo= request.getParameter("cartAdd");
        if(addTo!=null) {
        	if(!cart.containsKey(addTo)) {
        		cart.put(addTo, 1);
        	}
        	else {
        		cart.put(addTo, cart.get(addTo)+1);
        	}        	
        }
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		
    		
    		out.println("");
    		PreparedStatement qry = null;
    		String qry2="";
    		
    		if (fulltextSearch != null) {
    			qry2 = "SELECT * FROM movies as m Left JOIN  ratings as r ON r.movieId = m.id left join (select movieId, title, group_concat(name) as genres from genres_in_movies left join genres on genres_in_movies.genreId = genres.id left join movies on genres_in_movies.movieId = movies.id Group by movies.id ) as gm ON gm.movieId = m.id left join ( select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies left join stars on stars_in_movies.starId = stars.id left join movies on stars_in_movies.movieId = movies.id Group by movies.id ) as sm ON sm.movieId = m.id WHERE MATCH (m.title) AGAINST (? IN BOOLEAN MODE)"        ;		 
       		 if(currentPage<0) {currentPage=0;}
    			int Qsize = 0;
    			 qry = connection.prepareStatement(qry2);
        		qry.setString(1, parsedSearch);
        		ResultSet SizeQ = qry.executeQuery() ;
        		if ( SizeQ!= null) 
        		{
        		  SizeQ.beforeFirst();
        		  SizeQ.last();
        		  Qsize = SizeQ.getRow();
        		}
        		if(currentPage>Qsize) {currentPage-=pCount;}
       		 
       		 
       		 if(sortBy.equals("r.rating")&&direction.equals("DESC"))
       				qry2+=	" ORDER BY r.rating is null, r.rating DESC limit ? , ? ;";
       			if(sortBy.equals("r.rating")&&direction.equals("ASC"))
       				qry2+=	" ORDER BY r.rating is null, r.rating ASC limit ? , ? ;";
       			if(sortBy.equals("m.title")&&direction.equals("DESC"))
       				qry2+=	" ORDER BY m.title is null, m.title DESC limit ? , ? ;";
       			if(sortBy.equals("m.title")&&direction.equals("ASC"))
       				qry2+=	" ORDER BY m.title is null,  m.title ASC limit ? , ? ;";
       		 qry = connection.prepareStatement(qry2);

       		qry.setString(1, parsedSearch);
       		qry.setInt(2, currentPage);
       		qry.setInt(3, pCount);
    		}
    		
    		else if(genreBrowse!=null) {
qry2+="SELECT * FROM movies as m Left JOIN ratings as r ON r.movieId = m.id join ( select movieId, title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id join movies on genres_in_movies.movieId = movies.id Group by movies.id HAVING FIND_IN_SET( ? , genres) > 0 ) as gm ON gm.movieId = m.id join ( select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id join movies on stars_in_movies.movieId = movies.id Group by movies.id) as sm ON sm.movieId = m.id";


if(currentPage<0) {currentPage=0;}
	int Qsize = 0;
	 qry = connection.prepareStatement(qry2);
	qry.setString(1, genreBrowse);
	ResultSet SizeQ = qry.executeQuery() ;
	if ( SizeQ!= null) 
	{
	  SizeQ.beforeFirst();
	  SizeQ.last();
	  Qsize = SizeQ.getRow();
	}
	if(currentPage>Qsize) {currentPage-=pCount;}


if(sortBy.equals("r.rating")&&direction.equals("DESC"))
	qry2+=	" ORDER BY r.rating is null, r.rating DESC limit ? , ? ;";
if(sortBy.equals("r.rating")&&direction.equals("ASC"))
	qry2+=	" ORDER BY r.rating is null, r.rating ASC limit ? , ? ;";
if(sortBy.equals("m.title")&&direction.equals("DESC"))
	qry2+=	" ORDER BY m.title is null, m.title DESC limit ? , ? ;";
if(sortBy.equals("m.title")&&direction.equals("ASC"))
	qry2+=	" ORDER BY m.title is null,  m.title ASC limit ? , ? ;";

    		 qry = connection.prepareStatement(qry2);
    		qry.setString(1, genreBrowse);
    		qry.setInt(2, currentPage);
    		qry.setInt(3, pCount);

    		}
    		else if(titleBrowse!=null) {
        		 qry2 = "SELECT * FROM movies as m Left JOIN  ratings as r ON r.movieId = m.id join (select movieId, title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id join movies on genres_in_movies.movieId = movies.id Group by movies.id ) as gm ON gm.movieId = m.id join ( select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id join movies on stars_in_movies.movieId = movies.id Group by movies.id ) as sm ON sm.movieId = m.id WHERE SUBSTRING(m.title, 1, 1) = ? ";
        		 
        		 if(currentPage<0) {currentPage=0;}
     			int Qsize = 0;
     			 qry = connection.prepareStatement(qry2);
         		qry.setString(1, titleBrowse);
         		ResultSet SizeQ = qry.executeQuery() ;
         		if ( SizeQ!= null) 
         		{
         		  SizeQ.beforeFirst();
         		  SizeQ.last();
         		  Qsize = SizeQ.getRow();
         		}
         		if(currentPage>Qsize) {currentPage-=pCount;}
        		 
        		 
        		 if(sortBy.equals("r.rating")&&direction.equals("DESC"))
        				qry2+=	" ORDER BY r.rating is null, r.rating DESC limit ? , ? ;";
        			if(sortBy.equals("r.rating")&&direction.equals("ASC"))
        				qry2+=	" ORDER BY r.rating is null, r.rating ASC limit ? , ? ;";
        			if(sortBy.equals("m.title")&&direction.equals("DESC"))
        				qry2+=	" ORDER BY m.title is null, m.title DESC limit ? , ? ;";
        			if(sortBy.equals("m.title")&&direction.equals("ASC"))
        				qry2+=	" ORDER BY m.title is null,  m.title ASC limit ? , ? ;";
        		 qry = connection.prepareStatement(qry2);

        		qry.setString(1, titleBrowse);
        		qry.setInt(2, currentPage);
        		qry.setInt(3, pCount);
        	}
    		else {
    			 qry2 = "SELECT * FROM movies as m Left JOIN  ratings as r ON r.movieId = m.id join (select movieId, title, group_concat(name) as genres from genres_in_movies join genres on genres_in_movies.genreId = genres.id join movies on genres_in_movies.movieId = movies.id Group by movies.id ) as gm ON gm.movieId = m.id join ( select movieId, title, group_concat(name) as stars, group_concat(starId) as starID from stars_in_movies join stars on stars_in_movies.starId = stars.id join movies on stars_in_movies.movieId = movies.id Group by movies.id ) as sm ON sm.movieId = m.id ";
    			String WHERE="WHERE ";
    			if(titleSearch!=null) {WHERE+="m.title LIKE ? ";}
    			
    			if ( directorSearch != null) {
   				 if(WHERE!="WHERE " ) {WHERE+="AND ";}
   				 WHERE +=  "m.director LIKE ? ";
   			 }
   			 if ( starSearch!=null) {
   				 if(WHERE!="WHERE ") {WHERE+="AND ";}
   				 WHERE +=  "stars LIKE ? ";
   			 }
   			if (yearSearch!=null) {
  				 if(WHERE!="WHERE ") {WHERE+="AND ";}
  				 WHERE += "m.year = ? ";
  			 }
			if(!WHERE.equals("WHERE ")) {qry2+=WHERE;}

   			int n=1;
    		qry = connection.prepareStatement(qry2);
    		if(titleSearch!=null) {qry.setString(n, "%"+titleSearch+"%");n++;}
    		if(directorSearch!=null) {qry.setString(n, "%"+directorSearch+"%");n++;}
    		if(starSearch!=null) {qry.setString(n, "%"+starSearch+"%");n++;}
    		if(yearSearch!=null) {qry.setString(n, yearSearch);n++;}
    		if(currentPage<0) {currentPage=0;}
			int Qsize = 0;
    		ResultSet SizeQ = qry.executeQuery() ;
    		if ( SizeQ!= null) 
    		{
    		  SizeQ.beforeFirst();
    		  SizeQ.last();
    		  Qsize = SizeQ.getRow();
    		}

    		if(currentPage>Qsize) {currentPage-=pCount;}
    		
   		
    			if(sortBy.equals("r.rating")&&direction.equals("DESC"))
    				qry2+=	" ORDER BY r.rating is null, r.rating DESC limit ? , ? ;";
    			if(sortBy.equals("r.rating")&&direction.equals("ASC"))
    				qry2+=	" ORDER BY r.rating is null, r.rating ASC limit ? , ? ;";
    			if(sortBy.equals("m.title")&&direction.equals("DESC"))
    				qry2+=	" ORDER BY m.title is null, m.title DESC limit ? , ? ;";
    			if(sortBy.equals("m.title")&&direction.equals("ASC"))
    				qry2+=	" ORDER BY m.title is null,  m.title ASC limit ? , ? ;";
    			n=1;
        		qry = connection.prepareStatement(qry2);
        		if(titleSearch!=null) {qry.setString(n, "%"+titleSearch+"%");n++;}
        		if(directorSearch!=null) {qry.setString(n, "%"+directorSearch+"%");n++;}
        		if(starSearch!=null) {qry.setString(n, "%"+starSearch+"%");n++;}
        		if(yearSearch!=null) {qry.setString(n, yearSearch);n++;}

        		qry.setInt(n, currentPage);
        		n++;
        		qry.setInt(n, pCount);
    			
    		}
    		
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
            request.getSession().setAttribute("cart", cart);
            request.getSession().setAttribute("fulltextSearch", fulltextSearch);
    		
    		ResultSet resultSet = qry.executeQuery();
    		
    		//set up body
    		out.println("<body>");
    		out.println("<button onclick=\"window.location.href = \'/project1/MainPage\';\"><h4>Main Page</h4></button>");
    		out.println("<button onclick=\"window.location.href = \'/project1/ShoppingCart\';\"><h4>Checkout</h4></button>");
    		
    		out.println("<center>");
    		out.println("<h1>Movies</h1>");
    		out.println("</center>");
    		
    		out.println("<right>");
    		out.println("<form class=\"form1\" action=\"/project1/MovieServlet\"> Movies per page <select name=\"pCount\"> <option value=\"\" selected disabled hidden>limit</option><option value=5>5</option><option value=10>10</option><option value=20>20</option><option value=50>50</option><option value=100>100</option> </select> <input type=\"submit\"></form>");
    		out.print("<form class=\"form2\" method=\"get\"> Sort by:  <button name=\"sort\" type=\"submit\" value=\"r.rating\">Rating</button><button name=\"sort\" type=\"submit\" value=\"m.title\">Title</button><br>In order: <button name=\"direction\" type=\"submit\" value=\"ASC\">Ascend</button><button name=\"direction\" type=\"submit\" value=\"DESC\">Descend</button></form>");
    		out.println("</right>");
    		
    		out.println("<center>");
    	//	out.println(qry2);

    		out.println("<table border>");
    		// set up table header
    		out.println("<tr>");
    		out.println("<th>Title</th>");
    		out.println("<th>Year</th>");
    		out.println("<th>Director</th>");
    		out.println("<th>Genres</th>");
    		out.println("<th>Stars</th>");
    		out.println("<th>Rating</th>");
    		out.println("</tr>");
    		
    		while (resultSet.next()) {	
    			String title = resultSet.getString("title");
    			String year = resultSet.getString("year");
    			String director = resultSet.getString("director");
    			String genres="";
    			if(resultSet.getString("genres")!=null)
    				genres = resultSet.getString("genres");
    			String stars="";
    			if(resultSet.getString("stars")!=null)
    				stars = resultSet.getString("stars");
    			String starId="";
    			if(resultSet.getString("starID")!=null)
    				starId= resultSet.getString("starID");
    			String rating = resultSet.getString("rating");
    			
    			helperFunct help = new helperFunct();
    			
    			out.println("<tr>");
    			out.println("<td width=\"20%\"><a href = \"/project1/SingleMovieServlet?query="+ resultSet.getString("id")+"\">" + title + "</a></td>");
    			out.println("<td>" + year + "</td>");
    			out.println("<td>" + director + "</td>");
    			out.println("<td>" + help.listerG(genres, genres, "/project1/MovieServlet") + "</td>");
    			out.println("<td>" + help.lister(stars, starId, "/project1/SingleStarServlet") + "</td>");
    			out.println("<td>" + rating + "</td>");
    			out.println("<td width=\"10%\"><button onclick=\"window.location.href = \'/project1/MovieServlet?cartAdd="+resultSet.getString("id")+"\';\" >Add to Cart</button></td>");
    			out.println("</tr>");
    		}
    		
    		out.println("</table>");
    		out.println("<br>");
    		out.println("<form action=\"/project1/MovieServlet\" method=\"get\"><button name = \"pageMsg\" value=\"prev\">Prev</button>"+ " " + "<button name = \"pageMsg\" value=\"next\">Next</button>"+"</form>");
    		out.println("</center>");
    		out.println("</body>");
    		
    		resultSet.close();
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
