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

import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 8/09/2018.
 */

public class GroupObject {
    public StudentGroupsResults StudentGroupsResults;

    public GroupObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentGroupsResults").item(0);

        if (root.getElementsByTagName("NumberRecords").getLength() == 0) {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("invalid key")) {
                throw new Exceptions.ExpiredToken();
            } else {
                throw new Exceptions.UnknownServerError();
            }
        }

        NodeList years = root.getElementsByTagName("Years").item(0).getChildNodes();

        StudentGroupsResults results = new StudentGroupsResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();
        results.NumberYears = root.getElementsByTagName("NumberYears").item(0).getTextContent();

        for (int i = 0; i != years.getLength(); i++) {
            Element yearElement = (Element) years.item(i);
            Year year = new Year();
            year.index = yearElement.getAttribute("index");
            year.Grid = yearElement.getElementsByTagName("Grid").item(0).getTextContent();
            year.NumberGroups = yearElement.getElementsByTagName("NumberGroups").item(0).getTextContent();

            NodeList groups = yearElement.getElementsByTagName("Groups").item(0).getChildNodes();
            for (int j = 0; j != groups.getLength(); j++) {
                Element groupElement = (Element) groups.item(j);
                Group group = new Group();
                group.index = groupElement.getAttribute("index");
                group.Name = groupElement.getElementsByTagName("Name").item(0).getTextContent();
                group.Teacher = groupElement.getElementsByTagName("Teacher").item(0).getTextContent();
                if (groupElement.getElementsByTagName("Comment").getLength() > 0)
                    group.Comment = groupElement.getElementsByTagName("Comment").item(0).getTextContent();
                else
                    group.Comment = "";

                year.Groups.add(group);
            }

            results.Years.add(year);
        }

        StudentGroupsResults = results;
    }

    public class StudentGroupsResults {
        public String AccessLevel;

        public String NumberYears;

        public List<Year> Years = new ArrayList<>();

        public String ErrorCode;

        public String apiversion;

        public String NumberRecords;
    }

    public class Group {
        public String Name;

        public String index;

        public String Teacher;

        public String Comment;
    }

    public class Year {
        public List<Group> Groups = new ArrayList<>();

        public String index;

        public String NumberGroups;

        public String Grid;
    }
}
