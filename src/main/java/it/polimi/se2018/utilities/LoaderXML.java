package it.polimi.se2018.utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class is used for loading an xml file and offer some basic method for its manipulation
 * @author Framonti
 */
public abstract class LoaderXML {

    private Document doc;

    /**
     * Loads an xml file as a DOM Document
     * @param pathName The path to the xml file
     */
    public LoaderXML(String pathName) {

        File inputFile = new File(pathName);

        //create a DOM Document from the File
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            if(dBuilder != null)
                doc = dBuilder.parse(inputFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();
    }

    /**
     * Gets a NodeList given a particular tagName
     * @param tagName A tag of the xml file
     * @return A NodeList of Elements
     */
    private NodeList getNodeList(String tagName) {

        return this.doc.getElementsByTagName(tagName);
    }

    /**
     * Gets a List of String representing the contents of all the attribute "id" in a tag
     * @param tagAttribute A tag of the xml file
     * @return A List of String representing the contents of all the attribute "id" in a tag
     */
    public List<String> getStringListAttribute(String tagAttribute){

        List<String> listToReturn = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName(tagAttribute);
        for(int i = 0; i< nodeList.getLength(); i++){

            Element e = (Element) nodeList.item(i);
            listToReturn.add(e.getAttribute("id"));
        }
        return listToReturn;
    }

    /**
     * Gets a List of String representing the contents of all the same tag in an xml file
     * @param tagName A tag of the xml file
     * @return A List of String representing the contents of all the same tag in an xml file
     */
    //@return a List of items from an xml file with the tag "tagName"
    public List<String> getStringList(String tagName) {

        List<String> listToReturn = new ArrayList<>();
        NodeList nodeList = this.getNodeList(tagName);
        for (int i = 0; i < nodeList.getLength(); i++) {

            listToReturn.add(nodeList.item(i).getTextContent());
        }
        return listToReturn;
    }

}
