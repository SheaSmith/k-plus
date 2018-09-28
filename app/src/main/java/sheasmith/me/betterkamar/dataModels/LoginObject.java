package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class LoginObject implements Serializable
{
    public LogonResults LogonResults;

    public LoginObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.InvalidUsernamePassword, Exceptions.UnknownServerError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("LogonResults").item(0);
        if (root.getElementsByTagName("Success").getLength() != 0) {
            LogonResults results = new LogonResults();
            results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
            results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
            results.Success = root.getElementsByTagName("Success").item(0).getTextContent();
            results.LogonLevel = root.getElementsByTagName("LogonLevel").item(0).getTextContent();
            results.CurrentStudent = root.getElementsByTagName("CurrentStudent").item(0).getTextContent();
            results.Key = root.getElementsByTagName("Key").item(0).getTextContent();

            LogonResults = results;
        }
        else {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("The Username & Password do not appear to match a user on record. Please re-enter your Username and Password.")) {
                throw new Exceptions.InvalidUsernamePassword();
            }
            else {
                throw new Exceptions.UnknownServerError();
            }
        }


    }

    public class LogonResults implements Serializable
    {
        public String AccessLevel;

        public String Key;

        public String LogonLevel;

        public String ErrorCode;

        public String Success;

        public String CurrentStudent;

        public String apiversion;
    }
}