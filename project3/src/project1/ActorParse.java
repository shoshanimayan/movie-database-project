package project1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class ActorParse extends DefaultHandler {
	 private String tempVal;
	 private String tempCode;
	 private String fN;
	 private String lN;
	  HashMap<String, Star> starList;
	 private Star tempStar= new Star();
	 ActorParse(HashMap<String, Star> s){
		 this.starList=s;
		 
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
	            sp.parse("actors63.xml", this);

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
	        if (qName.equalsIgnoreCase("actor")) {
	        	tempStar= new Star();
	        	tempCode="";
	        	fN="";
	        	lN="";
	         //   System.out.println("open");
	        }
	    }
	    
	    public void characters(char[] ch, int start, int length) throws SAXException {
	        tempVal = new String(ch, start, length);
//            System.out.println(tempVal);

	    }
	    
	    public void endElement(String uri, String localName, String qName) throws SAXException {

	    	if (qName.equalsIgnoreCase("actor")) {
	    	//  System.out.println("close");  
	    		if(!tempStar.Sname.equals("") ){
		    	
	            starList.put(tempStar.Sname,tempStar);
	            }
	    	}
	    	if (qName.equalsIgnoreCase("stagename")) {
		    	//  if(tempVal==null) {System.out.println("problem");   }
	    		
		            tempStar.setSName(tempVal);
		    	}
	    	else if (qName.equalsIgnoreCase("familyname")) {
	    		//tempStar.setName(tempVal);
	    		lN = tempVal;
    		//	System.out.println(tempVal);

		    	}
	    	else if (qName.equalsIgnoreCase("firstname")) {
	    		tempStar.setName(tempVal+" "+lN);
	    		//fN = tempVal;
    		//	System.out.println(tempVal);

		    	}
	    	else if (qName.equalsIgnoreCase("dob")) {
	    		if(!tempVal.equals("")) {
		    		try{
		    			//System.out.println(tempVal);
		    			tempStar.setYear(Integer.parseInt(tempVal));
		    		}
		    		catch(Exception e) {
		    		System.out.println("problem with year value for actor "+tempStar.name+": "+tempVal);	
		    		}
	    		} 
		    }
	       
	    }

	    public static void main(String[] args) {
	     
	    }
	

}
