package project1;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mysql.jdbc.StringUtils;

public class XMLParse {
	Document dom; //movie data
	Document dom2; //actor data

	

    public void runExample() {

        //parse the xml file and get the dom object
        parseXmlFile();

        //get each employee element and create a Employee object
        parseDocument();

        //Iterate through the list and print the data
       // printData();

    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            dom = db.parse("mains243.xml");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        //get the root elememt
    	 dom.getDocumentElement().normalize();
    	 Element docEle = dom.getDocumentElement();

         //get a nodelist of <employee> elements
         NodeList nl = docEle.getElementsByTagName("directorfilms");
         if (nl != null && nl.getLength() > 0) {
             for (int i = 0; i < nl.getLength(); i++) {

                 //get the employee element
                 Element el = (Element) nl.item(i);
                 
               //NodeList n = el.getElementsByTagName("Director");
                 NodeList director = el.getChildNodes();
                 NodeList Films = director.item(1).getChildNodes();
                 //System.out.println(Films.item(0).getNodeName());
                 if(Films!=null && Films.getLength()>0) {
                	 
                	// for (int x = 0; i < Films.getLength(); i++) {
                		 Element id = (Element) Films.item(1).getChildNodes().item(0);
                		 System.out.println(id.getFirstChild().getNodeValue());
                		 Element title = (Element) Films.item(1).getChildNodes().item(1);
                		 if(title.hasChildNodes())
                			 System.out.println(title.getFirstChild().getNodeValue());


                		 
                	// }
                }

                 }
                 //  if (nl != null && nl.getLength() > 0) {
                 //      Element el = (Element) nl.item(0);
                  //     textVal = el.getFirstChild().getNodeValue();
                  // }

                 //get the Employee object
                // Employee e = getEmployee(el);

                 //add it to list
              //   myEmpls.add(e);
             }
         }
      
        
        
        //get a nodelist of <employee> elements
        //NodeList nl = docEle.getElementsByTagName("Employee");
        //if (nl != null && nl.getLength() > 0) {
           // for (int i = 0; i < nl.getLength(); i++) {

                //get the employee element
              //  Element el = (Element) nl.item(i);

                //get the Employee object
//                Employee e = getEmployee(el);

                //add it to list
  //              myEmpls.add(e);
     //       }
     //   }
    

    /**
     * I take an employee element and read the values in, create
     * an Employee object and return it
     * 
     * @param empEl
     * @return
     */
    //private Employee getEmployee(Element empEl) {

        //for each <employee> element get text or int values of 
        //name ,id, age and name
        //String name = getTextValue(empEl, "Name");
        //int id = getIntValue(empEl, "Id");
        //int age = getIntValue(empEl, "Age");

      //  String type = empEl.getAttribute("type");

        //Create a new Employee with the value read from the xml nodes
        //Employee e = new Employee(name, id, age, type);

        //return e;
   // }

    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is name I will return John
     * 
     * @param ele
     * @param tagName
     * @return
     */
   // private String getTextValue(Element ele, String tagName) {
    //    String textVal = null;
     //   NodeList nl = ele.getElementsByTagName(tagName);
      //  if (nl != null && nl.getLength() > 0) {
      //      Element el = (Element) nl.item(0);
       //     textVal = el.getFirstChild().getNodeValue();
       // }

      //  return textVal;
  //  }

    /**
     * Calls getTextValue and returns a int value
     * 
     * @param ele
     * @param tagName
     * @return
     */
   // private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
      //  return Integer.parseInt(getTextValue(ele, tagName));
    //}

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {

      
    }

    public static void main(String[] args) {
        String n = "nm0000001";
        n=n.substring(2);
        System.out.println(n.replaceAll("^0*", ""));
        
       
    }
}
