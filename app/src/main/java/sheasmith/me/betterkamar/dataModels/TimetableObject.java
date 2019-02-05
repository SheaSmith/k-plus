/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 18/01/19 11:45 PM
 */

package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 8/09/2018.
 */

public class TimetableObject implements Serializable {
    public StudentTimetableResults StudentTimetableResults;

    public TimetableObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError, Exceptions.AccessDenied {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentTimetableResults").item(0);

        if (root.getElementsByTagName("NumberRecords").getLength() == 0) {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("invalid key")) {
                throw new Exceptions.ExpiredToken();
            } else if (error.equalsIgnoreCase("access denied") || error.equalsIgnoreCase("access denied (1)")) {
                throw new Exceptions.AccessDenied();
            } else {
                throw new Exceptions.UnknownServerError();
            }
        }

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
                        if (parts.length > 3)
                            classPeriod.Teacher = parts[3];
                        else
                            classPeriod.Teacher = "--";

                        if (parts.length > 4)
                            classPeriod.Room = parts[4];
                        else
                            classPeriod.Room = "--";

                        week.Classes.get(dayNumber).add(classPeriod);
                    }

                    week.Term = Integer.parseInt(periods[0].split("-")[1]);
                }
            }

            student.Timetable.add(week);

            results.Student = student;

        }
        StudentTimetableResults = results;
    }

    public class StudentTimetableResults implements Serializable {
        public String AccessLevel;

        public String TTGrid;

        public String ErrorCode;

        public Student Student;

        public String apiversion;

        public String NumberRecords;
    }

    public class Student implements Serializable {
        public String index;

        public String IDNumber;

        public String Tutor;

        public ArrayList<Week> Timetable = new ArrayList<>();

        public String Level;
    }

    public class Week implements Serializable {
        public HashMap<Integer, List<Class>> Classes = new HashMap<>();
        public int WeekNumber;
        public int Term;
    }

    public class Class implements Serializable {
        // No idea what this is. Appears to either be C or O
        public String GridType = "";
        // 'Unique' subject identifier.
        public String SubjectNumber = "";
        public String SubjectCode = "";
        public String Teacher = "";
        public String Room = "";

        public char attendance = '.';
    }


}
