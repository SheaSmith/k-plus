/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
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

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class EventsObject implements Serializable
{
    public EventsResults EventsResults;

    public EventsObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError, Exceptions.AccessDenied {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("EventsResults").item(0);

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

        if (root.getElementsByTagName("Events").getLength() != 0) {
            NodeList events = root.getElementsByTagName("Events").item(0).getChildNodes();

            EventsResults results = new EventsResults();
            results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
            results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
            results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

            for (int i = 0; i != events.getLength(); i++) {
                if (events.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element eventElement = (Element) events.item(i);
                    Event event = new Event();
                    event.index = eventElement.getAttribute("index");
                    event.Title = eventElement.getElementsByTagName("Title").item(0).getTextContent();
                    event.Location = eventElement.getElementsByTagName("Location").item(0).getTextContent();
                    event.Details = eventElement.getElementsByTagName("Details").item(0).getTextContent();
                    event.Priority = eventElement.getElementsByTagName("Priority").item(0).getTextContent();
                    event.Public = eventElement.getElementsByTagName("Public").item(0).getTextContent();
                    event.Student = eventElement.getElementsByTagName("Student").item(0).getTextContent();
                    event.CG1 = eventElement.getElementsByTagName("CG1").item(0).getTextContent();
                    event.CG2 = eventElement.getElementsByTagName("CG2").item(0).getTextContent();
                    event.Staff = eventElement.getElementsByTagName("Staff").item(0).getTextContent();
                    event.Colour = eventElement.getElementsByTagName("Colour").item(0).getTextContent();
                    event.ColourLabel = eventElement.getElementsByTagName("ColourLabel").item(0).getTextContent();
                    event.DateTimeInfo = eventElement.getElementsByTagName("DateTimeInfo").item(0).getTextContent();
                    event.DateTimeStart = eventElement.getElementsByTagName("DateTimeStart").item(0).getTextContent();
                    event.DateTimeFinish = eventElement.getElementsByTagName("DateTimeFinish").item(0).getTextContent();
                    event.Start = eventElement.getElementsByTagName("Start").item(0).getTextContent();
                    event.Finish = eventElement.getElementsByTagName("Finish").item(0).getTextContent();

                    results.Events.add(event);
                }
            }


            EventsResults = results;
        } else {
            EventsResults = new EventsResults();
        }
    }

    public class EventsResults implements Serializable
    {
        public String AccessLevel;

        public ArrayList<Event> Events = new ArrayList<>();

        public String ErrorCode;

        public String apiversion;

        public String NumberRecords;
    }

    public class Event implements Serializable
    {
        public String index;

        public String Staff;

        public String Start;

        public String Details;

        public String DateTimeStart;

        public String Finish;

        public String Location;

        public String Title;

        public String Priority;

        public String Student;

        public String CG1;

        public String Colour;

        public String ColourLabel;

        public String CG2;

        public String DateTimeFinish;

        public String DateTimeInfo;

        public String Public;
    }
}

