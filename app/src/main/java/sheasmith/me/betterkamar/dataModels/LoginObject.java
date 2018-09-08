package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class LoginObject
{
    public LogonResults LogonResults;

    public LoginObject(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("LogonResults").item(0);

        LogonResults results = new LogonResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.Success = root.getElementsByTagName("Success").item(0).getTextContent();
        results.LogonLevel = root.getElementsByTagName("LogonLevel").item(0).getTextContent();
        results.CurrentStudent = root.getElementsByTagName("CurrentStudent").item(0).getTextContent();
        results.Key = root.getElementsByTagName("Key").item(0).getTextContent();
    }

    public class LogonResults
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