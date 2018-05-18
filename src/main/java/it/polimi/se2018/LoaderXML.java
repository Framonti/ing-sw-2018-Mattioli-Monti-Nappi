package it.polimi.se2018;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class LoaderXML {

    private Document doc;

    //Constructor
    public LoaderXML(String pathName)
    {
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

    //Get a NodeList from a tagname
    public NodeList getNodeList(String tagName)
    {
        return this.doc.getElementsByTagName(tagName);
    }

    //@return a List of items from an xml file with the tag "tagName"
    public List<String> getStringList(String tagName)
    {
        List<String> listToReturn = new ArrayList<>();
        NodeList nodeList = this.getNodeList(tagName);
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            listToReturn.add(nodeList.item(i).getTextContent());
        }
        return listToReturn;
    }

}
