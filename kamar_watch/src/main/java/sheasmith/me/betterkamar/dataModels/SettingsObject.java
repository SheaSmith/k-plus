/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 14/03/19 6:41 PM
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class SettingsObject implements Serializable
{
    public SettingsResults SettingsResults;

    public SettingsObject(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("SettingsResults").item(0);

        SettingsResults results = new SettingsResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.SettingsVersion = root.getElementsByTagName("SettingsVersion").item(0).getTextContent();
        results.MiniOSVersion = root.getElementsByTagName("MiniOSVersion").item(0).getTextContent();
        results.MinAndroidVersion = root.getElementsByTagName("MinAndroidVersion").item(0).getTextContent();
        results.StudentsAllowed = root.getElementsByTagName("StudentsAllowed").item(0).getTextContent();
        results.StaffAllowed = root.getElementsByTagName("StaffAllowed").item(0).getTextContent();
        results.StudentsSavedPasswords = root.getElementsByTagName("StudentsSavedPasswords").item(0).getTextContent();
        results.StaffSavedPasswords = root.getElementsByTagName("StaffSavedPasswords").item(0).getTextContent();
        results.SchoolName = root.getElementsByTagName("SchoolName").item(0).getTextContent();
        results.LogoPath = root.getElementsByTagName("LogoPath").item(0).getTextContent();
        results.AssessmentTypesShown = root.getElementsByTagName("AssessmentTypesShown").item(0).getTextContent();
        results.ShowEnrolledEntries = root.getElementsByTagName("ShowEnrolledEntries").item(0).getTextContent();

        Element userAccess = (Element) root.getElementsByTagName("UserAccess").item(0);
        NodeList users = userAccess.getElementsByTagName("User");
        for (int i = 0 ; i != users.getLength() ; i++) {
            Element userElement = (Element) users.item(i);
            User user = new User();

            user.Notices = userElement.getElementsByTagName("Notices").getLength() == 0 ? "0" : userElement.getElementsByTagName("Notices").item(0).getTextContent();
            user.Events = userElement.getElementsByTagName("Events").getLength() == 0 ? "0" : userElement.getElementsByTagName("Events").item(0).getTextContent();
            user.Details = userElement.getElementsByTagName("Details").getLength() == 0 ? "0" : userElement.getElementsByTagName("Details").item(0).getTextContent();
            user.Timetable = userElement.getElementsByTagName("Timetable").getLength() == 0 ? "0" : userElement.getElementsByTagName("Timetable").item(0).getTextContent();
            user.Attendance = userElement.getElementsByTagName("Attendance").getLength() == 0 ? "0" : userElement.getElementsByTagName("Attendance").item(0).getTextContent();
            user.NCEA = userElement.getElementsByTagName("NCEA").getLength() == 0 ? "0" : userElement.getElementsByTagName("NCEA").item(0).getTextContent();
            user.Results = userElement.getElementsByTagName("Results").getLength() == 0 ? "0" : userElement.getElementsByTagName("Results").item(0).getTextContent();
            user.Groups = userElement.getElementsByTagName("Groups").getLength() == 0 ? "0" : userElement.getElementsByTagName("Groups").item(0).getTextContent();
            user.Awards = userElement.getElementsByTagName("Awards").getLength() == 0 ? "0" : userElement.getElementsByTagName("Awards").item(0).getTextContent();
            user.Pastoral = userElement.getElementsByTagName("Pastoral").getLength() == 0 ? "0" : userElement.getElementsByTagName("Pastoral").item(0).getTextContent();
        }

        SettingsResults = results;
    }

    public class SettingsResults implements Serializable
    {
        public String AccessLevel;

        public String AssessmentTypesShown;

        public String MinAndroidVersion;

        public String StaffAllowed;

        public String StaffSavedPasswords;

        public String SchoolName;

        public String StudentsAllowed;

        public String apiversion;

        public String MiniOSVersion;

        public String LogoPath;

        public String StudentsSavedPasswords;

        public String SettingsVersion;

        public String ShowEnrolledEntries;

        public String ErrorCode;

        public UserAccess UserAccess;
    }
    public class UserAccess implements Serializable
    {
        public ArrayList<User> User = new ArrayList<>();
        public CalendarSettings CalendarSettings;
    }
    public class CalendarSettings implements Serializable
    {
        public String orange;

        public String auburn;

        public String red;

        public String purple;

        public String aero;

        public String blue;

        public String green;

        public String tangerine;

        public String violet;

        public String teal;

        public String black;

        public String amazon;
    }
    public class User implements Serializable
    {
        public String index;

        public String Notices;

        public String Events;
        public String Details;
        public String Timetable;
        public String Attendance;
        public String NCEA;
        public String Results;
        public String Groups;
        public String Awards;
        public String Pastoral;
    }

}
