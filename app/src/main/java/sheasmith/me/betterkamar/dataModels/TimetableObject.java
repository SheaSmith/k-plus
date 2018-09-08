package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TheDiamondPicks on 8/09/2018.
 */

public class TimetableObject {
    public StudentTimetableResults StudentTimetableResults;

    public TimetableObject(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentTimetableResults").item(0);
        Element studentElement = (Element) root.getElementsByTagName("Students").item(0).getFirstChild();
        NodeList weeks = studentElement.getElementsByTagName("TimetableData").item(0).getChildNodes();

        StudentTimetableResults results = new StudentTimetableResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.TTGrid = root.getElementsByTagName("TTGrid").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        Student student = new Student();
        student.IDNumber = studentElement.getElementsByTagName("IDNumber").item(0).getTextContent();
        student.Level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
        student.Tutor = studentElement.getElementsByTagName("Tutor").item(0).getTextContent();

        for (int i = 0; i != weeks.getLength(); i++) {
            Element weekElement = (Element) weeks.item(i);
            Week week = new Week();

            week.WeekNumber = Integer.parseInt(weekElement.getTagName().replace("W", ""));

            NodeList days = weekElement.getChildNodes();
            for (int j = 0; j != days.getLength(); j++) {
                Element day = (Element) days.item(j);

                int dayNumber = Integer.parseInt(day.getTagName().replace("D", ""));
                week.Classes.put(dayNumber, new ArrayList<Class>());
                String[] periods = day.getTextContent().split(Pattern.quote("|"));
                for (int k = 1; k != periods.length; k++) {
                    String period = periods[k];
                    String[] parts = period.split("-");
                    if (parts.length <= 1)
                        week.Classes.get(dayNumber).add(new Class());
                    else {
                        Class classPeriod = new Class();
                        classPeriod.GridType = parts[0];
                        classPeriod.SubjectNumber = parts[1];
                        classPeriod.SubjectCode = parts[2];
                        classPeriod.Teacher = parts[3];
                        classPeriod.Room = parts[4];

                        week.Classes.get(dayNumber).add(classPeriod);
                    }

                    week.Term = Integer.parseInt(parts[0].split("-")[1]);
                }
            }

            student.Timetable.add(week);

            results.Student = student;

            StudentTimetableResults = results;
        }
    }

    public class StudentTimetableResults {
        public String AccessLevel;

        public String TTGrid;

        public String ErrorCode;

        public Student Student;

        public String apiversion;

        public String NumberRecords;
    }

    public class Student {
        public String index;

        public String IDNumber;

        public String Tutor;

        public List<Week> Timetable = new ArrayList<>();

        public String Level;
    }

    public class Week {
        public HashMap<Integer, List<Class>> Classes = new HashMap<>();
        public int WeekNumber;
        public int Term;
    }

    public class Class {
        // No idea what this is. Appears to either be C or O
        public String GridType = "";
        // 'Unique' subject identifier.
        public String SubjectNumber = "";
        public String SubjectCode = "";
        public String Teacher = "";
        public String Room = "";
    }


}
