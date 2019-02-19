package project1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MainParse  extends DefaultHandler {
	HashMap<String,Star> s;
	String tempVal;
	String tempCode;
	MainParse(HashMap<String,Star> s){
		this.s= s; 
	}
	 public void runExample() {
	        parseDocument();
	    }
	    private void parseDocument() {

	        //get a factory
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        try {

	            //get a new instance of parser
	            SAXParser sp = spf.newSAXParser();

	            //parse the file and also register this class for call backs
	            sp.parse("casts124.xml", this);

	        } catch (SAXException se) {
	            se.printStackTrace();
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	        } catch (IOException ie) {
	            ie.printStackTrace();
	        }
	    }
	    public void characters(char[] ch, int start, int length) throws SAXException {
	        tempVal = new String(ch, start, length);
//            System.out.println(tempVal);

	    }
	    
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        //reset
	        tempVal = "";
	        if (qName.equalsIgnoreCase("m")) {//System.out.println("open"); 
	        tempCode="";}
	       
	    }
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	        if (qName.equalsIgnoreCase("m")) {
	       // 	System.out.println("close");
	        }
	        if (qName.equalsIgnoreCase("f")) {
	        //	System.out.println(tempVal);
	        	tempCode = tempVal;
	        }
	        if (qName.equalsIgnoreCase("a")) {
	        	//System.out.println(tempVal);
	        	if(s.containsKey(tempVal)) {
	        		s.get(tempVal).addMov(tempCode);
	        	} 
	        	else {
	        		if(!tempVal.equals("")) {
	        		s.put(tempVal, new Star());
	        		s.get(tempVal).setName(tempVal);
	        		s.get(tempVal).setSName(tempVal);
	        		s.get(tempVal).addMov(tempCode);
	        		}
	        	}
	        }
	    }

	  public static void main(String[] args) {
		  long startTime = System.currentTimeMillis();

		  
		  	HashMap<String,Star> s = new HashMap<String, Star>();
	        HashMap<String, movieI> m = new HashMap<String, movieI>();
	        HashMap<String, Genre> g = new HashMap<String, Genre>();
	        HashMap<String,Star> Fs = new HashMap<String, Star>();
	        HashMap<String, movieI> Fm = new HashMap<String, movieI>();
	        HashMap<String, Genre> Fg = new HashMap<String, Genre>();
		MovieParse p = new MovieParse(m,g);
		p.runExample();
		ActorParse a = new ActorParse(s);
		a.runExample();
		MainParse mp = new MainParse(s);
		mp.runExample();
		
		
		String loginUser = "root";
	    String loginPasswd = "espeon123";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        
        try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		// create database connection
    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    		// declare statement

    		HashSet<String> sInM = new HashSet<String>(); 
    		HashSet<String> gInM = new HashSet<String>(); 

    		
    		Integer maxM=0;
    		Integer maxS=0;
    		
    		String MQ; //to retrieve movie info
    		String SQ; //to retrieve star infor
    		String GQ; //to retrieve genre info
    		String GM; // genre in movie
    		String SM;//actor in movie
    		
    		//maps to hold values 
    		HashMap<String,movieI> Movies= new HashMap<String,movieI>();
   // 		HashMap<String,String> Stars= new HashMap<String,String>();
    		HashMap<String,Integer> Genres= new HashMap<String,Integer>();
    	;
    		HashSet<String> G_M= new HashSet<String>();
    		HashSet<String> FG_M= new HashSet<String>();

    		HashSet<String> S_M= new HashSet<String>();
    		HashSet<String> FS_M= new HashSet<String>();


    		
    		MQ = "SELECT id, title, director, year from movies";
    //		SQ = "SELECT id, name from stars";
    		GQ = "SELECT id, name from genres";
    		GM = "SELECT * from genres_in_movies";
    		SM = "SELECT * from stars_in_movies";
    		

    		PreparedStatement qry = connection.prepareStatement(MQ);
    		ResultSet resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    			movieI temp = new movieI();
    			temp.setDId(resultSet.getString("id"));
    			temp.setDirector(resultSet.getString("director"));
    			temp.setYear(Integer.parseInt(resultSet.getString("year")));
    			Movies.put(resultSet.getString("title"),temp );
    		}
    		//System.out.println(Movies.size());
    		
    	//	qry = connection.prepareStatement(SQ);
    		//resultSet = qry.executeQuery();
    		
    		//while (resultSet.next()) {
    		//	Stars.put(resultSet.getString("name"),resultSet.getString("id") );
    	//	}
    		//System.out.println(Stars.size());
    		
    		qry = connection.prepareStatement(GQ);
    		resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    			Genres.put(resultSet.getString("name"),Integer.parseInt(resultSet.getString("id")) );
    		}
    		//System.out.println(Genres.size());
    		
    		qry = connection.prepareStatement(GM);
    		resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    		
    			G_M.add(resultSet.getString("genreId")+","+resultSet.getString("movieId"));	
    		}
    		//System.out.println(G_M.size());
    		
    		qry = connection.prepareStatement(SM);
    		resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    		
    			S_M.add(resultSet.getString("StarId")+","+resultSet.getString("movieId"));	
    		}
    		
    		qry = connection.prepareStatement("SELECT max(id) from stars");
    		resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    		
    			String temp = resultSet.getString("max(id)").substring(2);
    			temp.replaceAll("^0*", "");
    			maxS = Integer.parseInt(temp);		
    		}
    		
    		qry = connection.prepareStatement("SELECT max(id) from movies");
    		resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    		
    			String temp = resultSet.getString("max(id)").substring(2);
    			temp.replaceAll("^0*", "");
    			maxM = Integer.parseInt(temp);
    			
    		}
    		
    		
   		 connection.setAutoCommit(false);

    		//information filter
    		
    		
    		for(movieI i: m.values()) {
    			if(Movies.containsKey(i.Title)) {
    				if(i.year==Movies.get(i.Title).year  && i.Director.equals(Movies.get(i.Title).Director))
    					m.get(i.Id).setDId(Movies.get(i.Title).DId);
    				else {
    					String temp="";
        				maxM+=1;
        				for(int x=0;x<(7-Integer.toString(maxM).length());x++) {
        					temp+="0";
        				}
        				temp+=Integer.toString(maxM);
        				temp = "tt"+temp;
        				m.get(i.Id).setDId(temp);
        				//Movies.put(i.Title, m.get(i.Id));
        				//System.out.println(temp);
        				Fm.put(i.Id, m.get(i.Id));
    				}
    			}	
    			else {
    				String temp="";
    				maxM+=1;
    				for(int x=0;x<(7-Integer.toString(maxM).length());x++) {
    					temp+="0";
    				}
    				temp+=Integer.toString(maxM);
    				temp = "tt"+temp;
    				m.get(i.Id).setDId(temp);
    				Movies.put(i.Title, m.get(i.Id));
    				//System.out.println(temp);
    				Fm.put(i.Id, m.get(i.Id));
    			}
    		}
        	//System.out.println(s.containsKey(""));

    		for(Star i: s.values()) {
    			//if(Stars.containsKey(i.name)) {
    				//System.out.println(i.birthYear);
    			//	if(s.get(i.Sname)!=null)
    			//		s.get(i.Sname).setID(Stars.get(i.name));
    			//	else {  s.get(i.name).setID(Stars.get(i.name));}
    			//}	
    			//else {
    				String temp="";
    				maxS+=1;
    				for(int x=0;x<(7-Integer.toString(maxS).length());x++) {
    					temp+="0";
    				}
    				temp+=Integer.toString(maxS);
    				temp = "nm"+temp;
    			//	System.out.println(temp);
    				//System.out.println(i.Sname);
    				if(i.Sname.equals("")==false) {
    				//	System.out.println(i.Sname.equals(""));
    				s.get(i.Sname).setID(temp);
    				//Stars.put(i.name,temp);
    				Fs.put(i.name, s.get(i.Sname));
    				}
    				else {
    					s.get(i.name).setID(temp);
        				//Stars.put(i.name,temp);
        				Fs.put(i.name, s.get(i.name));}
    			//}
    		}
    		System.out.println("adding movies");
    		qry = connection.prepareStatement("insert into movies values (?,?,?,?)");
    		int x=0;
    		int batchSize=1000;
    		for(movieI i: Fm.values()) {
    			x++;
    			qry.setString(1, i.DId);
    			if(i.Title!=null)
    				qry.setString(2, i.Title);
    			else { qry.setString(2, "");
}
    			if(i.year!=null) {
    			qry.setInt(3, i.year);}
    			else {qry.setInt(3, 0);}
    			if(i.Director!=null)
    				qry.setString(4, i.Director);
    			else {qry.setString(4, "");}
    			qry.addBatch();
    			if(x%batchSize==0||x==Fm.size()) {
    				qry.executeBatch();
    				connection.commit();
    			}
    		}
    		x=0;
    		System.out.println("adding stars");
    		qry = connection.prepareStatement("insert into stars values (?,?,?)");
    		for(Star i : Fs.values()) {
    			x++;
    			if(i.ID!=null) {
    				qry.setString(1, i.ID);
    			}
    			else {System.out.println(i.name);}
    			if(i.name!=null) {
    				qry.setString(2, i.name);
    			}else {qry.setString(2, "");}
    			if(i.birthYear!=null) {
    				qry.setInt(3, i.birthYear);
    			}else {qry.setNull(3, java.sql.Types.INTEGER);}
    			qry.addBatch();
    			if(x%batchSize==0||x==Fs.size()) {
    				qry.executeBatch();
    				connection.commit();
    			}
    		
    		}
    		
    		Integer  max= Collections.max(Genres.values());
    		//System.out.println(max);
    		for(String i: g.keySet()) {
    			if(Genres.containsKey(i)) {
    				//max+=1;
    				g.get(i).setId(Genres.get(i));;
        			//System.out.println(i);
    			}	
    			else {
    				max+=1;
    				g.get(i).setId(max);
    				Fg.put(i, g.get(i));
    				Genres.put(i, max);
    			}
    		}
    		System.out.println("adding genres");

    		x=0;
    		qry = connection.prepareStatement("insert into genres values (?,?)");
    		for(Genre i : Fg.values()) {
    				qry.setInt(1, i.id);
    			if(i.name!=null) {
    				qry.setString(2, i.name);
    			}else {qry.setString(2, "");}
    			qry.addBatch();
    			if(x%batchSize==0||x==Fg.size()) {
    				qry.executeBatch();
    				connection.commit();
    			}
    		}
    		System.out.println("adding genres in movies");

    		qry = connection.prepareStatement("insert into genres_in_movies values (?,?)");
    		x=0;
    		for(Genre i : g.values()) {
    			for(String movie: i.moviesIn) {
    				if(m.containsKey(movie)) {
    					//System.out.println(i==null);
    					String temp = Integer.toString(i.id)+","+m.get(movie).DId;
    					if(!G_M.contains(temp)) {
    						//FG_M.add(temp);
    						x++;
    						qry.setInt(1,i.id);
    		    			qry.setString(2,m.get(movie).DId );
    		    			qry.addBatch();
    		    			if(x%1000==0) {qry.executeBatch();}
    						
    						G_M.add(temp);
    					}
    				}
    			}
    		}qry.executeBatch();
    	
    		//for(String i: s.keySet()) {
    		//	if(s.get(i).ID==null) {System.out.println(i);}
    		//}
    		System.out.println("adding stars in movives");

    		qry = connection.prepareStatement("insert into stars_in_movies values (?,?)");
    		int y=0;
    		for(Star i: s.values()) {

    			for(String movie: i.moviesIn) {
    				if(m.containsKey(movie)) {
    					//if(i.ID==null) {System.out.println(i.moviesIn.size());}
    					String temp = i.ID+","+m.get(movie).DId;
    					if(!S_M.contains(temp)) {
    						qry.setString(1, i.ID);
    						qry.setString(2, m.get(movie).DId);
    						qry.addBatch();
    						y++;
    						if(y%10000==0) {qry.executeBatch();}
    						//FS_M.add(temp);
    						S_M.add(temp);
    					}
    					//else {System.out.println(i.ID.equals(""));}
    				}
					//else {System.out.println(movie);}

    			}
    		} qry.executeBatch();
    		//System.out.println(FG_M.size());
    		//System.out.println(FS_M.size());

    		//// begin inserting 
    		
    		//genres
    		
    		//genres in movies
    		/*
    		x=0;
    		qry = connection.prepareStatement("insert into genres_in_movies values (?,?)");
    		for( String i: FG_M) {
    			String[] vals=i.split(",");
    			qry.setInt(1,Integer.parseInt(vals[0]));
    			qry.setString(2, vals[1]);
    			qry.addBatch();
    			if(x%batchSize==0||x==FG_M.size()) {
    				//System.out.println(i);
    				qry.executeBatch();
    				connection.commit();
    			}
    		}
    		*/
    		//x=0;
    		/*
    		qry = connection.prepareStatement("insert into stars_in_movies values (?,?)");
    		for( String i: FS_M) {
    			String[] vals=i.split(",");
    			qry.setString(1,vals[0]);
    			qry.setString(2, vals[1]);
    			qry.addBatch();
    			if(x%10000==0||x==FS_M.size()) {
    				//try {
    				qry.executeBatch();
    				connection.commit();
    			//	System.out.println(i);
    				//}
    				//catch(Exception e) {System.out.println(vals[1]);}
    				
    			}
    		}
    		*/
    		qry.close();
    		connection.setAutoCommit(true);
    		resultSet.close();
    		connection.close();
        		
		
        } catch (Exception e) {
    		e.printStackTrace();
        }

        
        Long endTime = System.currentTimeMillis();

		System.out.println("done");
        System.out.println("took: "+(endTime - startTime) );

		
		
	}
}

class movieI{  
    String Id; 
    String DId;
    String Title;
    String Director;
    Integer year;
   
    void movieI(){  
    	this.DId="";
        this.Id="";
        this.Title="";
        this.Director="";
        this.year=null;
        
    }  
    void setId(String id) {
    	this.Id= id;
    }
    void setDId(String id) {
    	this.DId= id;
    }
    void setTitle(String t) {
    	this.Title=t;
    }
    void setDirector(String d) {
    	this.Director=d;
    }
    void setYear(Integer y) {
    	this.year=y;
    }
    
}

class Star {
	String ID;
	String name;
	String Sname;
	Integer birthYear;
	HashSet<String> moviesIn;
	
	Star(){
		this.ID="";
		this.Sname="";
		this.name="";
		this.birthYear=null;
		moviesIn= new HashSet<String>();
	}	
	void setID(String id) {
		this.ID=id;
	}
	void setName(String n) {
		this.name=n;
	}
	void setSName(String n) {
		this.name=n;
	}
	void setYear(Integer y) {
		this.birthYear=y;
	}
	void addMov(String movieID) {
		this.moviesIn.add(movieID);
	}
		
}

class Genre{
	String name;
	
	HashSet<String> moviesIn;
	Integer id;
	
	Genre(){
		this.name="";
		this.moviesIn= new HashSet<String>();
		this.id = null;
	}
	void setName(String n) {
		this.name=n;
	}
	
	void setId(Integer x) {
		this.id=x;
	}
	void addMovie(String MovieID) {
		this.moviesIn.add(MovieID);
	}
	
}

 


