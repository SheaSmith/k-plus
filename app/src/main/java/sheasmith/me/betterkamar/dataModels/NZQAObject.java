package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NZQAObject
{
    public StudentOfficialResultsResults StudentOfficialResultsResults;

    public NZQAObject(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentOfficialResultsResults").item(0);
        NodeList types = root.getElementsByTagName("Types").item(0).getChildNodes();

        StudentOfficialResultsResults results = new StudentOfficialResultsResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();
        results.NumberTypes = root.getElementsByTagName("NumberTypes").item(0).getTextContent();

        for (int i = 0; i != types.getLength(); i++) {
            Element typeElement = (Element) types.item(i);
            Type type = new Type();
            type.TypeCode = typeElement.getElementsByTagName("TypeCode").item(0).getTextContent();
            type.NumberQuals = typeElement.getElementsByTagName("NumberQuals").item(0).getTextContent();
            type.index = typeElement.getAttribute("index");

            NodeList qualifications = typeElement.getElementsByTagName("Qualifications").item(0).getChildNodes();
            for (int j = 0; j != qualifications.getLength(); j++) {
                Element qualificationElement = (Element) qualifications.item(j);
                Qualification qualification = new Qualification();

                qualification.index = qualificationElement.getAttribute("index");
                qualification.Year = qualificationElement.getElementsByTagName("Year").item(0).getTextContent();
                qualification.Ref = qualificationElement.getElementsByTagName("Ref").item(0).getTextContent();
                qualification.Endorse = qualificationElement.getElementsByTagName("Endorse").item(0).getTextContent();
                qualification.Level = qualificationElement.getElementsByTagName("Level").item(0).getTextContent();
                qualification.Title = qualificationElement.getElementsByTagName("Title").item(0).getTextContent();

                type.Qualifications.add(qualification);
            }

            results.Types.add(type);
        }

        StudentOfficialResultsResults = results;
    }

    public class StudentOfficialResultsResults
    {
        public String AccessLevel;

        public String NumberTypes;

        public List<Type> Types = new ArrayList<>();

        public String ErrorCode;

        public String apiversion;

        public String NumberRecords;
    }

    public class Qualification
    {
        public String Year;

        public String Ref;

        public String index;

        public String Level;

        public String Endorse;

        public String Title;
    }

    public class Type
    {
        public String index;

        public String TypeCode;

        public String NumberQuals;

        public List<Qualification> Qualifications = new ArrayList<>();
    }
}