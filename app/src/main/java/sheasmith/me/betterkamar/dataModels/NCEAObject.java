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

public class NCEAObject
{
    public StudentNCEASummaryResults StudentNCEASummaryResults;

    public NCEAObject(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentNCEASummaryResults").item(0);
        Element studentElement = (Element) ((Element) root.getElementsByTagName("Students").item(0)).getElementsByTagName("Student").item(0);

        StudentNCEASummaryResults results = new StudentNCEASummaryResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        Student student = new Student();

        CreditsTotal creditsTotal = new CreditsTotal();
        Element creditsTotalElement = (Element) studentElement.getElementsByTagName("CreditsTotal").item(0);
        creditsTotal.Achieved = creditsTotalElement.getElementsByTagName("Achieved").item(0).getTextContent();
        creditsTotal.Merit = creditsTotalElement.getElementsByTagName("Merit").item(0).getTextContent();
        creditsTotal.Excellence = creditsTotalElement.getElementsByTagName("Excellence").item(0).getTextContent();
        creditsTotal.Total = creditsTotalElement.getElementsByTagName("Total").item(0).getTextContent();
        creditsTotal.Attempted = creditsTotalElement.getElementsByTagName("Attempted").item(0).getTextContent();
        student.CreditsTotal = creditsTotal;

        CreditsInternal creditsInternal = new CreditsInternal();
        Element creditsInternalElement = (Element) studentElement.getElementsByTagName("CreditsInternal").item(0);
        creditsInternal.Achieved = creditsInternalElement.getElementsByTagName("Achieved").item(0).getTextContent();
        creditsInternal.Merit = creditsInternalElement.getElementsByTagName("Merit").item(0).getTextContent();
        creditsInternal.Excellence = creditsInternalElement.getElementsByTagName("Excellence").item(0).getTextContent();
        creditsInternal.Total = creditsInternalElement.getElementsByTagName("Total").item(0).getTextContent();
        creditsInternal.Attempted = creditsInternalElement.getElementsByTagName("Attempted").item(0).getTextContent();
        student.CreditsInternal = creditsInternal;

        CreditsExternal creditsExternal = new CreditsExternal();
        Element creditsExternalElement = (Element) studentElement.getElementsByTagName("CreditsExternal").item(0);
        creditsExternal.Achieved = creditsExternalElement.getElementsByTagName("Achieved").item(0).getTextContent();
        creditsExternal.Merit = creditsExternalElement.getElementsByTagName("Merit").item(0).getTextContent();
        creditsExternal.Excellence = creditsExternalElement.getElementsByTagName("Excellence").item(0).getTextContent();
        creditsExternal.Total = creditsExternalElement.getElementsByTagName("Total").item(0).getTextContent();
        creditsExternal.Attempted = creditsExternalElement.getElementsByTagName("Attempted").item(0).getTextContent();
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
                yearTotal.Achieved = yearTotalElement.getElementsByTagName("Merit").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("Excellence").getLength() != 0)
                yearTotal.Achieved = yearTotalElement.getElementsByTagName("Excellence").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("Total").getLength() != 0)
                yearTotal.Achieved = yearTotalElement.getElementsByTagName("Total").item(0).getTextContent();

            if (yearTotalElement.getElementsByTagName("Attempted").getLength() != 0)
                yearTotal.Achieved = yearTotalElement.getElementsByTagName("Attempted").item(0).getTextContent();

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
                levelTotal.Achieved = levelTotalElement.getElementsByTagName("Merit").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("Excellence").getLength() != 0)
                levelTotal.Achieved = levelTotalElement.getElementsByTagName("Excellence").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("Total").getLength() != 0)
                levelTotal.Achieved = levelTotalElement.getElementsByTagName("Total").item(0).getTextContent();

            if (levelTotalElement.getElementsByTagName("Attempted").getLength() != 0)
                levelTotal.Achieved = levelTotalElement.getElementsByTagName("Attempted").item(0).getTextContent();

            levelTotal.Level = levelTotalElement.getElementsByTagName("Level").item(0).getTextContent();

            student.LevelTotals.add(levelTotal);
        }

        results.Student = student;

        StudentNCEASummaryResults = results;
    }

    public class StudentNCEASummaryResults
    {
        public String AccessLevel;

        public String ErrorCode;

        public Student Student;

        public String apiversion;

        public String NumberRecords;
    }

    public class Student
    {
        public String index;

        public CreditsExternal CreditsExternal;

        public List<YearTotal> YearTotals = new ArrayList<>();

        public NCEA NCEA;

        public List<LevelTotal> LevelTotals = new ArrayList<>();

        public CreditsTotal CreditsTotal;

        public CreditsInternal CreditsInternal;
    }

    public class CreditsInternal
    {
        public String Achieved;

        public String Merit;

        public String Excellence;

        public String Total;

        public String Attempted;
    }

    public class CreditsTotal
    {
        public String Achieved;

        public String Merit;

        public String Excellence;

        public String Total;

        public String Attempted;
    }

    public class LevelTotal
    {
        public String index;

        public String Achieved;

        public String Merit;

        public String Excellence;

        public String Level;

        public String Total;

        public String Attempted;
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

        public String Merit;

        public String Achieved;

        public String Excellence;

        public String Total;

        public String Attempted;
    }

    public class CreditsExternal
    {
        public String Achieved;

        public String Merit;

        public String Excellence;

        public String Total;

        public String Attempted;
    }
}
