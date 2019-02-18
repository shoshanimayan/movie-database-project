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
	        		s.put(tempVal, new Star());
	        		s.get(tempVal).setName(tempVal);
	        		s.get(tempVal).addMov(tempCode);

	        	}
	        }
	    }

	  public static void main(String[] args) {
		  
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
    		HashMap<String,String> Movies= new HashMap<String,String>();
    		HashMap<String,String> Stars= new HashMap<String,String>();
    		HashMap<String,Integer> Genres= new HashMap<String,Integer>();
    	;
    		HashSet<String> G_M= new HashSet<String>();
    		HashSet<String> FG_M= new HashSet<String>();

    		HashSet<String> S_M= new HashSet<String>();
    		HashSet<String> FS_M= new HashSet<String>();


    		
    		MQ = "SELECT id, title from movies";
    		SQ = "SELECT id, name from stars";
    		GQ = "SELECT id, name from genres";
    		GM = "SELECT * from genres_in_movies";
    		SM = "SELECT * from stars_in_movies";
    		

    		PreparedStatement qry = connection.prepareStatement(MQ);
    		ResultSet resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    			Movies.put(resultSet.getString("title"),resultSet.getString("id") );
    		}
    		//System.out.println(Movies.size());
    		
    		qry = connection.prepareStatement(SQ);
    		resultSet = qry.executeQuery();
    		
    		while (resultSet.next()) {
    			Stars.put(resultSet.getString("name"),resultSet.getString("id") );
    		}
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
    		
    		
    		
    		//information filter
    		
    		
    		for(movieI i: m.values()) {
    			if(Movies.containsKey(i.Title)) {
    				m.get(i.Id).setDId(Movies.get(i.Title));
        			//System.out.println(i);
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
    				//System.out.println(temp);
    				Fm.put(i.Id, m.get(i.Id));
    			}
    		}
    		
    		for(String i: s.keySet()) {
    			if(Stars.containsKey(i)) {
    				s.get(i).setID(Movies.get(i));
    				//System.out.println("fine");

    			}	
    			else {
    				String temp="";
    				maxS+=1;
    				for(int x=0;x<(7-Integer.toString(maxS).length());x++) {
    					temp+="0";
    				}
    				temp+=Integer.toString(maxS);
    				temp = "nm"+temp;
    			//	System.out.println(temp);
    				s.get(i).setID(temp);
    				Fs.put(i, s.get(i));
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
    			}
    		}
    		
    	
    		for(Genre i : g.values()) {
    			for(String movie: i.moviesIn) {
    				if(m.containsKey(movie)) {
    					String temp = Integer.toString(i.id)+","+m.get(movie).DId;
    					if(!G_M.contains(temp)) {
    						FG_M.add(temp);
    					}
    					else {System.out.println(temp);}
    				}
    			}
    		}
    		
    		for(Star i: s.values()) {
    			for(String movie: i.moviesIn) {
    				if(m.containsKey(movie)) {
    					String temp = i.ID+","+m.get(movie).Id;
    					if(!S_M.contains(temp)) {
    						FS_M.add(temp);
    					}
    					else {System.out.println(temp);}
    				}
					//else {System.out.println(movie);}

    			}
    		}
    		
    		//// begin inserting 
    		qry = connection.prepareStatement("insert into movies values (?,?,?,?)");
    		int x=0;
    		int batchSize=1000;
    		for(movieI i: Fm.values()) {
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
    			}
    		}
    		

    		resultSet.close();
    		connection.close();
        		
		
        } catch (Exception e) {
    		e.printStackTrace();
        }

        
		
		System.out.println("done");
		
		
		
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

class p1{
	Integer i;
	String k;
	p1(Integer i, String k){
		this.i=i;
		this.k=k;
	}
}  

class p2{
	String i;
	String k;
	p2(String i, String k){
		this.i=i;
		this.k=k;
	}
}  

