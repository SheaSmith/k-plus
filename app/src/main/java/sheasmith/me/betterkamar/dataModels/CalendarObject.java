/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:53 PM
 */

package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

public class CalendarObject implements Serializable
{
    public CalendarResults CalendarResults;

    public CalendarObject(String xml) throws ParserConfigurationException, IOException, SAXException, Exceptions.ExpiredToken, Exceptions.UnknownServerError, Exceptions.AccessDenied {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("CalendarResults").item(0);

        if (root.getElementsByTagName("NumberRecords").getLength() == 0) {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("invalid key")) {
                throw new Exceptions.ExpiredToken();
            } else if (error.equalsIgnoreCase("access denied")) {
                throw new Exceptions.AccessDenied();
            } else {
                throw new Exceptions.UnknownServerError();
            }
        }

        NodeList days = root.getElementsByTagName("Days").item(0).getChildNodes();
        CalendarResults results = new CalendarResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        for (int i = 0 ; i != days.getLength() ; i++) {
            if (days.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element dayElement = (Element) days.item(i);
                Day day = new Day();
                day.index = dayElement.getAttribute("index");
                day.Date = dayElement.getElementsByTagName("Date").item(0).getTextContent();
                day.Status = dayElement.getElementsByTagName("Status").item(0).getTextContent();
                day.DayTT = dayElement.getElementsByTagName("DayTT").item(0).getTextContent();
                day.Term = dayElement.getElementsByTagName("Term").item(0).getTextContent();
                day.TermA = dayElement.getElementsByTagName("TermA").item(0).getTextContent();
                day.Week = dayElement.getElementsByTagName("Week").item(0).getTextContent();
                day.WeekA = dayElement.getElementsByTagName("WeekA").item(0).getTextContent();
                day.WeekYear = dayElement.getElementsByTagName("WeekYear").item(0).getTextContent();

                results.Days.add(day);
            }
        }

        CalendarResults = results;

    }

    public class CalendarResults implements Serializable
    {
        public String AccessLevel;

        public ArrayList<Day> Days = new ArrayList<>();

        public String ErrorCode;

        public String apiversion;

        public String NumberRecords;
    }

    public class Day implements Serializable
    {
        public String DayTT;

        public String Status;

        public String WeekA;

        public String index;

        public String Week;

        public String Date;

        public String WeekYear;

        public String Term;

        public String TermA;
    }
}
