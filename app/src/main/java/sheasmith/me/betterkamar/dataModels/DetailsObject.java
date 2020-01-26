/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 31/05/19 5:26 PM
 */

package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 8/09/2018.
 */

public class DetailsObject implements Serializable {
    public StudentDetailsResults StudentDetailsResults;

    public DetailsObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError, Exceptions.AccessDenied {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentDetailsResults").item(0);

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

        Element studentElement = (Element) root.getElementsByTagName("Students").item(0).getFirstChild();

        StudentDetailsResults results = new StudentDetailsResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        Student student = new Student();
        student.index = studentElement.getAttribute("index");
        student.StudentID = getIfNotNull(studentElement.getElementsByTagName("StudentID"));
        student.FirstName = getIfNotNull(studentElement.getElementsByTagName("FirstName"));
        student.ForeNames = getIfNotNull(studentElement.getElementsByTagName("ForeNames"));
        student.LastName = getIfNotNull(studentElement.getElementsByTagName("LastName"));
        student.FirstNameLegal = getIfNotNull(studentElement.getElementsByTagName("FirstNameLegal"));
        student.ForeNamesLegal = getIfNotNull(studentElement.getElementsByTagName("ForeNamesLegal"));
        student.LastNameLegal = getIfNotNull(studentElement.getElementsByTagName("LastNameLegal"));
        student.Gender = getIfNotNull(studentElement.getElementsByTagName("Gender"));
        student.Ethnicity = getIfNotNull(studentElement.getElementsByTagName("Ethnicity"));
        student.DateBirth = getIfNotNull(studentElement.getElementsByTagName("DateBirth"));
        student.Age = getIfNotNull(studentElement.getElementsByTagName("Age"));
        student.NSN = getIfNotNull(studentElement.getElementsByTagName("NSN"));
        student.StudentEmail = getIfNotNull(studentElement.getElementsByTagName("StudentEmail"));
        student.StudentSchoolEmail = getIfNotNull(studentElement.getElementsByTagName("StudentSchoolEmail"));
        student.HomePhone = getIfNotNull(studentElement.getElementsByTagName("HomePhone"));
        student.HomeAddress = getIfNotNull(studentElement.getElementsByTagName("HomeAddress"));

        student.ParentTitle = getIfNotNull(studentElement.getElementsByTagName("ParentTitle"));
        student.ParentEmail = getIfNotNull(studentElement.getElementsByTagName("ParentEmail"));

        if (studentElement.getElementsByTagName("HomePhoneB").getLength() != 0) {
            student.HomePhoneB = getIfNotNull(studentElement.getElementsByTagName("HomePhoneB"));
            student.HomeAddressB = getIfNotNull(studentElement.getElementsByTagName("HomeAddressB"));
            student.ParentTitleB = getIfNotNull(studentElement.getElementsByTagName("ParentTitleB"));
            student.ParentSalutationB = getIfNotNull(studentElement.getElementsByTagName("ParentSalutationB"));
            student.ParentEmailB = getIfNotNull(studentElement.getElementsByTagName("ParentEmailB"));
        }

        if (studentElement.getElementsByTagName("DoctorName").getLength() != 0) {
            student.DoctorName = getIfNotNull(studentElement.getElementsByTagName("DoctorName"));
            student.DoctorPhone = getIfNotNull(studentElement.getElementsByTagName("DoctorPhone"));
            student.DoctorAddress = getIfNotNull(studentElement.getElementsByTagName("DoctorAddress"));
        }

        if (studentElement.getElementsByTagName("DentistName").getLength() != 0) {
            student.DentistName = getIfNotNull(studentElement.getElementsByTagName("DentistName"));
            student.DentistPhone = getIfNotNull(studentElement.getElementsByTagName("DentistPhone"));
            student.DentistAddress = getIfNotNull(studentElement.getElementsByTagName("DentistAddress"));
        }

        student.AllowedPanadol = getIfNotNull(studentElement.getElementsByTagName("AllowedPanadol"));
        student.AllowedIbuprofen = getIfNotNull(studentElement.getElementsByTagName("AllowedIbuprofen"));
        student.HealthFlag = getIfNotNull(studentElement.getElementsByTagName("HealthFlag"));
        student.Medical = getIfNotNull(studentElement.getElementsByTagName("Medical"));
        student.Reactions = getIfNotNull(studentElement.getElementsByTagName("Reactions"));
        student.Vaccinations = getIfNotNull(studentElement.getElementsByTagName("Vaccinations"));
        student.SpecialCircumstances = getIfNotNull(studentElement.getElementsByTagName("SpecialCircumstances"));
        student.GeneralNotes = getIfNotNull(studentElement.getElementsByTagName("GeneralNotes"));
        student.HealthNotes = getIfNotNull(studentElement.getElementsByTagName("HealthNotes"));

        student.MotherRelation = getIfNotNull(studentElement.getElementsByTagName("MotherRelation"));
        student.MotherName = getIfNotNull(studentElement.getElementsByTagName("MotherName"));
        student.MotherStatus = getIfNotNull(studentElement.getElementsByTagName("MotherStatus"));
        student.MotherEmail = getIfNotNull(studentElement.getElementsByTagName("MotherEmail"));
        student.MotherPhoneHome = getIfNotNull(studentElement.getElementsByTagName("MotherPhoneHome"));
        student.MotherPhoneCell = getIfNotNull(studentElement.getElementsByTagName("MotherPhoneCell"));
        student.MotherPhoneWork = getIfNotNull(studentElement.getElementsByTagName("MotherPhoneWork"));
        student.MotherPhoneExtn = getIfNotNull(studentElement.getElementsByTagName("MotherPhoneExtn"));
        student.MotherOccupation = getIfNotNull(studentElement.getElementsByTagName("MotherOccupation"));
        student.MotherWorkAddress = getIfNotNull(studentElement.getElementsByTagName("MotherWorkAddress"));
        student.MotherNotes = getIfNotNull(studentElement.getElementsByTagName("MotherNotes"));

        student.FatherRelation = getIfNotNull(studentElement.getElementsByTagName("FatherRelation"));
        student.FatherName = getIfNotNull(studentElement.getElementsByTagName("FatherName"));
        student.FatherStatus = getIfNotNull(studentElement.getElementsByTagName("FatherStatus"));
        student.FatherEmail = getIfNotNull(studentElement.getElementsByTagName("FatherEmail"));
        student.FatherPhoneHome = getIfNotNull(studentElement.getElementsByTagName("FatherPhoneHome"));
        student.FatherPhoneCell = getIfNotNull(studentElement.getElementsByTagName("FatherPhoneCell"));
        student.FatherPhoneWork = getIfNotNull(studentElement.getElementsByTagName("FatherPhoneWork"));
        student.FatherPhoneExtn = getIfNotNull(studentElement.getElementsByTagName("FatherPhoneExtn"));
        student.FatherOccupation = getIfNotNull(studentElement.getElementsByTagName("FatherOccupation"));
        student.FatherWorkAddress = getIfNotNull(studentElement.getElementsByTagName("FatherWorkAddress"));
        student.FatherNotes = getIfNotNull(studentElement.getElementsByTagName("FatherNotes"));

        student.EmergencyName = getIfNotNull(studentElement.getElementsByTagName("EmergencyName"));
        student.EmergencyPhoneHome = getIfNotNull(studentElement.getElementsByTagName("EmergencyPhoneHome"));
        student.EmergencyPhoneCell = getIfNotNull(studentElement.getElementsByTagName("EmergencyPhoneCell"));
        student.EmergencyPhoneWork = getIfNotNull(studentElement.getElementsByTagName("EmergencyPhoneWork"));
        student.EmergencyPhoneExtn = getIfNotNull(studentElement.getElementsByTagName("EmergencyPhoneExtn"));
        student.EmergencyNotes = getIfNotNull(studentElement.getElementsByTagName("EmergencyNotes"));

        results.Student = student;
        StudentDetailsResults = results;
    }

    public class StudentDetailsResults implements Serializable {
        public String AccessLevel;

        public String ErrorCode;

        public Student Student;

        public String apiversion;

        public String NumberRecords;
    }

    public class Student implements Serializable {
        public String ParentSalutation;

        public String HealthNotes;

        public String EmergencyName;

        public String HomePhoneB;

        public String LastName;

        public String HomePhone;

        public String ParentTitleB;

        public String StudentID;

        public String SpecialCircumstances;

        public String ForeNames;

        public String NSN;

        public String MotherPhoneHome;

        public String Medical;

        public String ParentEmailB;

        public String MotherPhoneWork;

        public String EmergencyPhoneWork;

        public String EmergencyNotes;

        public String HomeAddressB;

        public String index;

        public String DentistName;

        public String FatherEmail;

        public String GeneralNotes;

        public String Reactions;

        public String FatherName;

        public String MotherName;

        public String MotherPhoneExtn;

        public String FatherWorkAddress;

        public String HealthFlag;

        public String FatherPhoneHome;

        public String FatherPhoneExtn;

        public String LastNameLegal;

        public String DentistAddress;

        public String Gender;

        public String EmergencyPhoneCell;

        public String EmergencyPhoneHome;

        public String ForeNamesLegal;

        public String EmergencyPhoneExtn;

        public String FatherOccupation;

        public String Age;

        public String ParentSalutationB;

        public String FatherRelation;

        public String Vaccinations;

        public String StudentEmail;

        public String FatherNotes;

        public String DentistPhone;

        public String ParentTitle;

        public String FirstNameLegal;

        public String DoctorPhone;

        public String DoctorAddress;

        public String FirstName;

        public String FatherPhoneWork;

        public String MotherOccupation;

        public String MotherRelation;

        public String HomeAddress;

        public String MotherNotes;

        public String Ethnicity;

        public String ParentEmail;

        public String AllowedIbuprofen;

        public String DoctorName;

        public String MotherEmail;

        public String StudentSchoolEmail;

        public String FatherPhoneCell;

        public String MotherPhoneCell;

        public String DateBirth;

        public String MotherWorkAddress;

        public String AllowedPanadol;

        public String FatherStatus;

        public String MotherStatus;
    }

    public String getIfNotNull(NodeList nodeList) {
        if (nodeList == null)
            return null;

        if (nodeList.item(0) == null)
            return null;

        if (nodeList.item(0).getTextContent() == null)
            return null;

        return nodeList.item(0).getTextContent();
    }
}
