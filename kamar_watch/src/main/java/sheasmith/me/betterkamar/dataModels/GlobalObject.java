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

                results.PeriodDefinitions.add(definition);
            }
        }

        GlobalsResults = results;
    }

    public class GlobalsResults implements Serializable
    {
        public String AccessLevel;

        public String ErrorCode;

        public ArrayList<PeriodDefinition> PeriodDefinitions = new ArrayList<>();

        public String apiversion;

        public String NumberRecords;
    }

    public class PeriodDefinition implements Serializable
    {
        public String index;

        public String PeriodTime;

        public String PeriodName;
    }
}