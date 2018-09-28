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

public class NCEAObject implements Serializable
{
    public StudentNCEASummaryResults StudentNCEASummaryResults;

    public NCEAObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentNCEASummaryResults").item(0);

        if (root.getElementsByTagName("NumberRecords").getLength() == 0) {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("invalid key")) {
                throw new Exceptions.ExpiredToken();
            } else {
                throw new Exceptions.UnknownServerError();
            }
        }

        Element studentElement = (Element) ((Element) root.getElementsByTagName("Students").item(0)).getElementsByTagName("Student").item(0);

        StudentNCEASummaryResults results = new StudentNCEASummaryResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        Student student = new Student();

        CreditsTotal creditsTotal = new CreditsTotal();
        if (studentElement.getElementsByTagName("CreditsTotal").getLength() > 0) {
            Element creditsTotalElement = (Element) studentElement.getElementsByTagName("CreditsTotal").item(0);
            if (creditsTotalElement.getElementsByTagName("Achieved").getLength() != 0)
                creditsTotal.Achieved = creditsTotalElement.getElementsByTagName("Achieved").item(0).getTextContent();
            if (creditsTotalElement.getElementsByTagName("Merit").getLength() != 0)
                creditsTotal.Merit = creditsTotalElement.getElementsByTagName("Merit").item(0).getTextContent();
            if (creditsTotalElement.getElementsByTagName("Excellence").getLength() != 0)
                creditsTotal.Excellence = creditsTotalElement.getElementsByTagName("Excellence").item(0).getTextContent();
            if (creditsTotalElement.getElementsByTagName("Total").getLength() != 0)
                creditsTotal.Total = creditsTotalElement.getElementsByTagName("Total").item(0).getTextContent();
            if (creditsTotalElement.getElementsByTagName("Attempted").getLength() != 0)
                creditsTotal.Attempted = creditsTotalElement.getElementsByTagName("Attempted").item(0).getTextContent();
            if (creditsTotalElement.getElementsByTagName("NotAchieved").getLength() != 0)
                creditsTotal.NotAchieved = creditsTotalElement.getElementsByTagName("NotAchieved").item(0).getTextContent();
        }
        student.CreditsTotal = creditsTotal;

        CreditsInternal creditsInternal = new CreditsInternal();
        if (studentElement.getElementsByTagName("CreditsInternal").getLength() > 0) {
            Element creditsInternalElement = (Element) studentElement.getElementsByTagName("CreditsInternal").item(0);
            if (creditsInternalElement.getElementsByTagName("Achieved").getLength() != 0)
                creditsInternal.Achieved = creditsInternalElement.getElementsByTagName("Achieved").item(0).getTextContent();
            if (creditsInternalElement.getElementsByTagName("Merit").getLength() != 0)
                creditsInternal.Merit = creditsInternalElement.getElementsByTagName("Merit").item(0).getTextContent();
            if (creditsInternalElement.getElementsByTagName("Excellence").getLength() != 0)
                creditsInternal.Excellence = creditsInternalElement.getElementsByTagName("Excellence").item(0).getTextContent();
            if (creditsInternalElement.getElementsByTagName("Total").getLength() != 0)
                creditsInternal.Total = creditsInternalElement.getElementsByTagName("Total").item(0).getTextContent();
            if (creditsInternalElement.getElementsByTagName("Attempted").getLength() != 0)
                creditsInternal.Attempted = creditsInternalElement.getElementsByTagName("Attempted").item(0).getTextContent();
            if (creditsInternalElement.getElementsByTagName("NotAchieved").getLength() != 0)
                creditsInternal.NotAchieved = creditsInternalElement.getElementsByTagName("NotAchieved").item(0).getTextContent();
        }
        student.CreditsInternal = creditsInternal;

        CreditsExternal creditsExternal = new CreditsExternal();
        if (studentElement.getElementsByTagName("CreditsExternal").getLength() > 0) {
            Element creditsExternalElement = (Element) studentElement.getElementsByTagName("CreditsExternal").item(0);
            if (creditsExternalElement.getElementsByTagName("Achieved").getLength() != 0)
                creditsExternal.Achieved = creditsExternalElement.getElementsByTagName("Achieved").item(0).getTextContent();
            if (creditsExternalElement.getElementsByTagName("Merit").getLength() != 0)
                creditsExternal.Merit = creditsExternalElement.getElementsByTagName("Merit").item(0).getTextContent();
            if (creditsExternalElement.getElementsByTagName("Excellence").getLength() != 0)
                creditsExternal.Excellence = creditsExternalElement.getElementsByTagName("Excellence").item(0).getTextContent();
            if (creditsExternalElement.getElementsByTagName("Total").getLength() != 0)
                creditsExternal.Total = creditsExternalElement.getElementsByTagName("Total").item(0).getTextContent();
            if (creditsExternalElement.getElementsByTagName("Attempted").getLength() != 0)
                creditsExternal.Attempted = creditsExternalElement.getElementsByTagName("Attempted").item(0).getTextContent();
            if (creditsExternalElement.getElementsByTagName("NotAchieved").getLength() != 0)
                creditsExternal.NotAchieved = creditsExternalElement.getElementsByTagName("NotAchieved").item(0).getTextContent();
        }
        student.CreditsExternal = creditsExternal;

        NCEA ncea = new NCEA();
        Element nceaElement = (Element) studentElement.getElementsByTagName("NCEA").item(0);
        ncea.L1NCEA = nceaElement.getElementsByTagName("L1NCEA").item(0).getTextContent();
        ncea.L2NCEA = nceaElement.getElementsByTagName("L2NCEA").item(0).getTextContent();
        ncea.L3NCEA = nceaElement.getElementsByTagName("L3NCEA").item(0).getTextContent();
        ncea.NCEAUELIT = nceaElement.getElementsByTagName("NCEAUELIT").item(0).getTextContent();
        ncea.NCEANUM = nceaElement.getElementsByTagName("NCEANUM").item(0).getTextContent();
        ncea.NCEAL1LIT = nceaElement.getElementsByTagName("NCEAL1LIT").item(0).getTextContent();
        student.NCEA = ncea;

        NodeList years = studentElement.getElementsByTagName("YearTotals").item(0).getChildNodes();
        for (int i = 0; i != years.getLength(); i++) {
            Element yearTotalElement = (Element) years.item(i);
            YearTotal yearTotal = new YearTotal();

            if (yearTotalElement.getElementsByTagName("Achieved").getLength() != 0)
                yearTotal.Achieved = yearTotalElement.getElementsByTagName("Achieved").item(0).getTextContent();
            if (yearTotalElement.getElementsByTagName("Merit").getLength() != 0)
                yearTotal.Merit = yearTotalElement.getElementsByTagName("Merit").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("Excellence").getLength() != 0)
                yearTotal.Excellence = yearTotalElement.getElementsByTagName("Excellence").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("Total").getLength() != 0)
                yearTotal.Total = yearTotalElement.getElementsByTagName("Total").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("Attempted").getLength() != 0)
                yearTotal.Attempted = yearTotalElement.getElementsByTagName("Attempted").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("NotAchieved").getLength() != 0)
                yearTotal.NotAchieved = yearTotalElement.getElementsByTagName("NotAchieved").item(0).getTextContent();

            yearTotal.Year = yearTotalElement.getElementsByTagName("Year").item(0).getTextContent();

            student.YearTotals.add(yearTotal);
        }

        NodeList levels = studentElement.getElementsByTagName("LevelTotals").item(0).getChildNodes();
        for (int i = 0; i != levels.getLength(); i++) {
            Element levelTotalElement = (Element) levels.item(i);
            LevelTotal levelTotal = new LevelTotal();

            if (levelTotalElement.getElementsByTagName("Achieved").getLength() != 0)
                levelTotal.Achieved = levelTotalElement.getElementsByTagName("Achieved").item(0).getTextContent();
            if (levelTotalElement.getElementsByTagName("Merit").getLength() != 0)
                levelTotal.Merit = levelTotalElement.getElementsByTagName("Merit").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("Excellence").getLength() != 0)
                levelTotal.Excellence = levelTotalElement.getElementsByTagName("Excellence").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("Total").getLength() != 0)
                levelTotal.Total = levelTotalElement.getElementsByTagName("Total").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("Attempted").getLength() != 0)
                levelTotal.Attempted = levelTotalElement.getElementsByTagName("Attempted").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("NotAchieved").getLength() != 0)
                levelTotal.NotAchieved = levelTotalElement.getElementsByTagName("NotAchieved").item(0).getTextContent();

            levelTotal.Level = levelTotalElement.getElementsByTagName("Level").item(0).getTextContent();

            student.LevelTotals.add(levelTotal);
        }

        results.Student = student;

        StudentNCEASummaryResults = results;
    }

    public class StudentNCEASummaryResults implements Serializable
    {
        public String AccessLevel;

        public String ErrorCode;

        public Student Student;

        public String apiversion;

        public String NumberRecords;
    }

    public class Student implements Serializable
    {
        public String index;

        public CreditsExternal CreditsExternal;

        public ArrayList<YearTotal> YearTotals = new ArrayList<>();

        public NCEA NCEA;

        public ArrayList<LevelTotal> LevelTotals = new ArrayList<>();

        public CreditsTotal CreditsTotal;

        public CreditsInternal CreditsInternal;
    }

    public class CreditsInternal implements Serializable
    {
        public String Achieved = "0";

        public String Merit = "0";

        public String Excellence = "0";

        public String Total = "0";

        public String Attempted = "0";

        public String NotAchieved = "0";
    }

    public class CreditsTotal
    {
        public String Achieved = "0";

        public String Merit = "0";

        public String Excellence = "0";

        public String Total = "0";

        public String Attempted = "0";

        public String NotAchieved = "0";
    }

    public class LevelTotal
    {
        public String index;

        public String Achieved = "0";

        public String Merit = "0";

        public String Excellence = "0";

        public String Level;

        public String Total = "0";

        public String Attempted = "0";

        public String NotAchieved = "0";
    }

    public class NCEA
    {
        public String NCEAL1LIT;

        public String L2NCEA;

        public String L3NCEA;

        public String L1NCEA;

        public String NCEANUM;

        public String NCEAUELIT;
    }

    public class YearTotal
    {
        public String Year;

        public String index;

        public String Merit = "0";

        public String Achieved = "0";

        public String Excellence = "0";

        public String Total = "0";

        public String Attempted = "0";

        public String NotAchieved = "0";
    }

    public class CreditsExternal
    {
        public String Achieved = "0";

        public String Merit = "0";

        public String Excellence = "0";

        public String Total = "0";

        public String Attempted = "0";

        public String NotAchieved = "0";
    }
}
