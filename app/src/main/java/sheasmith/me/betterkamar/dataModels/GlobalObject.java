/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 31/05/19 8:55 PM
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

public class GlobalObject implements Serializable
{
    public GlobalsResults GlobalsResults;

    public GlobalObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError, Exceptions.AccessDenied {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("GlobalsResults").item(0);

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

        NodeList periodDefinitions = root.getElementsByTagName("PeriodDefinitions").item(0).getChildNodes();
        NodeList startTimes = root.getElementsByTagName("StartTimes").item(0).getChildNodes();

        GlobalsResults results = new GlobalsResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        for (int i = 0; i != periodDefinitions.getLength(); i++) {
            if (periodDefinitions.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element periodDefElement = (Element) periodDefinitions.item(i);

                PeriodDefinition definition = new PeriodDefinition();
                definition.index = periodDefElement.getAttribute("index");
                definition.PeriodName = periodDefElement.getElementsByTagName("PeriodName").item(0).getTextContent();
                definition.PeriodTime = periodDefElement.getElementsByTagName("PeriodTime").item(0).getTextContent();

                if (definition.PeriodTime.equals(""))
                    definition.PeriodTime = "--";

                results.PeriodDefinitions.add(definition);
            }
        }

        for (int i = 0 ; i != startTimes.getLength() ; i++) {
            if (startTimes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element dayElement = (Element) startTimes.item(i);

                Day day = new Day();
                day.index = dayElement.getAttribute("index");

                NodeList periodTimes = dayElement.getChildNodes();
                for (int j = 0 ; j != periodTimes.getLength() ; j++) {
                    if (periodTimes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element periodTimeElement = (Element) periodTimes.item(j);

                        PeriodTime periodTime = new PeriodTime();
                        periodTime.index = periodTimeElement.getAttribute("index");
                        periodTime.time = periodTimeElement.getTextContent();

                        if (periodTime.time.equals(""))
                            periodTime.time = "--";

                        day.PeriodTimes.add(periodTime);
                    }
                }

                results.StartTimes.add(day);
            }
        }

        GlobalsResults = results;
    }

    public class GlobalsResults implements Serializable
    {
        public String AccessLevel;

        public String ErrorCode;

        public ArrayList<PeriodDefinition> PeriodDefinitions = new ArrayList<>();

        public ArrayList<Day> StartTimes = new ArrayList<>();

        public String apiversion;

        public String NumberRecords;
    }

    public class PeriodDefinition implements Serializable
    {
        public String index;

        public String PeriodTime;

        public String PeriodName;
    }

    public class Day implements Serializable
    {
        public String index;

        public ArrayList<PeriodTime> PeriodTimes = new ArrayList<>();
    }

    public class PeriodTime implements Serializable {
        public String index;
        public String time;
    }
}