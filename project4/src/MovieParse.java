import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class MovieParse extends DefaultHandler {
	 private String tempVal;
	 private movieI tempMovie;
	// private Genre genre;
	 HashMap<String, movieI> m;
	 HashMap<String, Genre> g;
	 MovieParse(HashMap<String, movieI> m, HashMap<String, Genre> g){
		this.m=m;
		this.g= g;
		 
	 }
	    //to maintain context
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
	            sp.parse("mains243.xml", this);

	        } catch (SAXException se) {
	            se.printStackTrace();
	        } catch (ParserConfigurationException pce) {
	            pce.printStackTrace();
	        } catch (IOException ie) {
	            ie.printStackTrace();
	        }
	    }

	
	    
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        //reset
	        tempVal = "";
	        if (qName.equalsIgnoreCase("film")) {
		        tempMovie= new movieI();
	        }
	    }
	    
	    public void characters(char[] ch, int start, int length) throws SAXException {
	        tempVal = new String(ch, start, length);
//            System.out.println(tempVal);

	    }
	    
	    public void endElement(String uri, String localName, String qName) throws SAXException {

	        if (qName.equalsIgnoreCase("film")) {
	          //  System.out.println("close");   
	            m.put(tempMovie.Id, tempMovie);
	            

	        } else if (qName.equalsIgnoreCase("fid")) {
	            //System.out.println(tempVal);
	            tempMovie.setId(tempVal);
	        }
	        else if (qName.equalsIgnoreCase("t")) {
	        	// System.out.println(tempVal);
		            tempMovie.setTitle(tempVal);
	           
	        }
	        else if (qName.equalsIgnoreCase("year")) {
	        	if(!tempVal.equals("")) {
	        		tempVal=tempVal.trim();
		        	try {
		                tempMovie.setYear(Integer.parseInt( tempVal ));
		            }
		            catch( Exception e ) {
		                System.out.println("problem in year value for movie "+tempMovie.Id+": "+tempVal);
		            }
	        	}
	        }
	        else if (qName.equalsIgnoreCase("dir")) {
	        //    System.out.println(tempVal);
	            if(tempMovie.Director!="")
	            	tempMovie.setDirector(tempVal);
	        }
	        else if(qName.equalsIgnoreCase("cat")) {
	        //	System.out.println(tempVal);
	        	boolean pass=true;
	        	if(!tempVal.equals("")) {
	        		//for(char i: tempVal.toCharArray()) {
	        		//	if(Character.isDigit(i)) {pass=false;}
	        		//}
		        	if(pass) {
		        		if(g.containsKey(tempVal)) {
			        		
				        	g.get(tempVal).addMovie(tempMovie.Id);
			        	}
			        	else {
			        		g.put(tempVal, new Genre());
			        		g.get(tempVal).setName(tempVal);
				        	g.get(tempVal).addMovie(tempMovie.Id);
			        	}
		        	}
		        	else {System.out.println("problem in genre value for movie "+tempMovie.Id+": "+tempVal);
}
	        		
	        	}
	        	
	        }
	       
	    }

	    public static void main(String[] args) {
	    	HashMap<String, movieI> m = new HashMap<String, movieI>();
	    	HashMap<String, Genre> g = new HashMap<String, Genre>();
	        MovieParse spe = new MovieParse(m,g);
	        HashSet<String > s = new HashSet<String>();
	        spe.runExample();
	        for(movieI i: m.values()) {
	        	if(s.contains(i.Title))
	        		System.out.println(i.Title);
	        	else {
	        	s.add(i.Title);}
	        	}
	    }
	

}
