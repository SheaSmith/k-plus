package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

public class ResultObject implements Serializable
{
    public StudentResultsResults StudentResultsResults;

    public ResultObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.UnknownServerError, Exceptions.ExpiredToken {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentResultsResults").item(0);

        if (root.getElementsByTagName("NumberRecords").getLength() == 0) {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("invalid key")) {
                throw new Exceptions.ExpiredToken();
            } else {
                throw new Exceptions.UnknownServerError();
            }
        }

        NodeList levels = root.getElementsByTagName("ResultLevels").item(0).getChildNodes();

        StudentResultsResults results = new StudentResultsResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();
        results.NumberResultLevels = root.getElementsByTagName("NumberResultLevels").item(0).getTextContent();
        results.StudentID = root.getElementsByTagName("StudentID").item(0).getTextContent();

        for (int i = 0; i != levels.getLength(); i++) {
            Element levelElement = (Element) levels.item(i);
            ResultLevel resultLevel = new ResultLevel();
            resultLevel.index = levelElement.getAttribute("index");
            resultLevel.NumberResults = levelElement.getElementsByTagName("NumberResults").item(0).getTextContent();
            resultLevel.NCEALevel = levelElement.getElementsByTagName("NCEALevel").item(0).getTextContent();

            NodeList resultsNodes = levelElement.getElementsByTagName("Results").item(0).getChildNodes();
            for (int j = 0; j != resultsNodes.getLength(); j++) {
                Element resultElement = (Element) resultsNodes.item(j);
                Result result = new Result();
                result.index = resultElement.getAttribute("index");
                result.Number = resultElement.getElementsByTagName("Number").item(0).getTextContent();
                result.Version = resultElement.getElementsByTagName("Version").item(0).getTextContent();
                result.Grade = resultElement.getElementsByTagName("Grade").item(0).getTextContent();
                result.Title = resultElement.getElementsByTagName("Title").item(0).getTextContent();
                result.SubField = resultElement.getElementsByTagName("SubField").item(0).getTextContent();
                result.Credits = resultElement.getElementsByTagName("Credits").item(0).getTextContent();
                result.CreditsPassed = resultElement.getElementsByTagName("CreditsPassed").item(0).getTextContent();
                result.ResultPublished = resultElement.getElementsByTagName("ResultPublished").item(0).getTextContent();

                resultLevel.Results.add(result);
            }

            results.ResultLevels.add(resultLevel);
        }

        StudentResultsResults = results;
    }

    public class StudentResultsResults implements Serializable
    {
        public String AccessLevel;

        public String NumberResultLevels;

        public String ErrorCode;

        public ArrayList<ResultLevel> ResultLevels = new ArrayList<>();

        public String apiversion;

        public String NumberRecords;

        public String StudentID;
    }
    public class Result implements Serializable
    {
        public String SubField;

        public String CreditsPassed;

        public String index;

        public String Grade;

        public String Credits;

        public String Number;

        public String ResultPublished;

        public String Version;

        public String Title;
    }

    public class ResultLevel implements Serializable
    {
        public String NCEALevel;

        public String index;

        public String NumberResults;

        public ArrayList<Result> Results = new ArrayList<>();
    }


}