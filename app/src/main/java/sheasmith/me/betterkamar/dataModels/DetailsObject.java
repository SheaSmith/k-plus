package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 8/09/2018.
 */

public class DetailsObject {
    public StudentDetailsResults StudentDetailsResults;

    public DetailsObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError {
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
        student.StudentID = studentElement.getElementsByTagName("StudentID").item(0).getTextContent();
        student.FirstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
        student.ForeNames = studentElement.getElementsByTagName("ForeNames").item(0).getTextContent();
        student.LastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
        student.FirstNameLegal = studentElement.getElementsByTagName("FirstNameLegal").item(0).getTextContent();
        student.ForeNamesLegal = studentElement.getElementsByTagName("ForeNamesLegal").item(0).getTextContent();
        student.LastNameLegal = studentElement.getElementsByTagName("LastNameLegal").item(0).getTextContent();
        student.Gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
        student.Ethnicity = studentElement.getElementsByTagName("Ethnicity").item(0).getTextContent();
        student.DateBirth = studentElement.getElementsByTagName("DateBirth").item(0).getTextContent();
        student.Age = studentElement.getElementsByTagName("Age").item(0).getTextContent();
        student.NSN = studentElement.getElementsByTagName("NSN").item(0).getTextContent();
        student.StudentEmail = studentElement.getElementsByTagName("StudentEmail").item(0).getTextContent();
        student.StudentSchoolEmail = studentElement.getElementsByTagName("StudentSchoolEmail").item(0).getTextContent();
        student.HomePhone = studentElement.getElementsByTagName("HomePhone").item(0).getTextContent();
        student.HomeAddress = studentElement.getElementsByTagName("HomeAddress").item(0).getTextContent();

        student.ParentTitle = studentElement.getElementsByTagName("ParentTitle").item(0).getTextContent();
        student.ParentEmail = studentElement.getElementsByTagName("ParentEmail").item(0).getTextContent();

        student.HomePhoneB = studentElement.getElementsByTagName("HomePhoneB").item(0).getTextContent();
        student.HomeAddressB = studentElement.getElementsByTagName("HomeAddressB").item(0).getTextContent();
        student.ParentTitleB = studentElement.getElementsByTagName("ParentTitleB").item(0).getTextContent();
        student.ParentSalutationB = studentElement.getElementsByTagName("ParentSalutationB").item(0).getTextContent();
        student.ParentEmailB = studentElement.getElementsByTagName("ParentEmailB").item(0).getTextContent();

        student.DoctorName = studentElement.getElementsByTagName("DoctorName").item(0).getTextContent();
        student.DoctorPhone = studentElement.getElementsByTagName("DoctorPhone").item(0).getTextContent();
        student.DoctorAddress = studentElement.getElementsByTagName("DoctorAddress").item(0).getTextContent();

        student.DentistName = studentElement.getElementsByTagName("DentistName").item(0).getTextContent();
        student.DentistPhone = studentElement.getElementsByTagName("DentistPhone").item(0).getTextContent();
        student.DentistAddress = studentElement.getElementsByTagName("DentistAddress").item(0).getTextContent();

        student.AllowedPanadol = studentElement.getElementsByTagName("AllowedPanadol").item(0).getTextContent();
        student.AllowedIbuprofen = studentElement.getElementsByTagName("AllowedIbuprofen").item(0).getTextContent();
        student.HealthFlag = studentElement.getElementsByTagName("HealthFlag").item(0).getTextContent();
        student.Medical = studentElement.getElementsByTagName("Medical").item(0).getTextContent();
        student.Reactions = studentElement.getElementsByTagName("Reactions").item(0).getTextContent();
        student.Vaccinations = studentElement.getElementsByTagName("Vaccinations").item(0).getTextContent();
        student.SpecialCircumstances = studentElement.getElementsByTagName("SpecialCircumstances").item(0).getTextContent();
        student.GeneralNotes = studentElement.getElementsByTagName("GeneralNotes").item(0).getTextContent();
        student.HealthNotes = studentElement.getElementsByTagName("HealthNotes").item(0).getTextContent();

        student.MotherRelation = studentElement.getElementsByTagName("MotherRelation").item(0).getTextContent();
        student.MotherName = studentElement.getElementsByTagName("MotherName").item(0).getTextContent();
        student.MotherStatus = studentElement.getElementsByTagName("MotherStatus").item(0).getTextContent();
        student.MotherEmail = studentElement.getElementsByTagName("MotherEmail").item(0).getTextContent();
        student.MotherPhoneHome = studentElement.getElementsByTagName("MotherPhoneHome").item(0).getTextContent();
        student.MotherPhoneCell = studentElement.getElementsByTagName("MotherPhoneCell").item(0).getTextContent();
        student.MotherPhoneWork = studentElement.getElementsByTagName("MotherPhoneWork").item(0).getTextContent();
        student.MotherPhoneExtn = studentElement.getElementsByTagName("MotherPhoneExtn").item(0).getTextContent();
        student.MotherOccupation = studentElement.getElementsByTagName("MotherOccupation").item(0).getTextContent();
        student.MotherWorkAddress = studentElement.getElementsByTagName("MotherWorkAddress").item(0).getTextContent();
        student.MotherNotes = studentElement.getElementsByTagName("MotherNotes").item(0).getTextContent();

        student.FatherRelation = studentElement.getElementsByTagName("FatherRelation").item(0).getTextContent();
        student.FatherName = studentElement.getElementsByTagName("FatherName").item(0).getTextContent();
        student.FatherStatus = studentElement.getElementsByTagName("FatherStatus").item(0).getTextContent();
        student.FatherEmail = studentElement.getElementsByTagName("FatherEmail").item(0).getTextContent();
        student.FatherPhoneHome = studentElement.getElementsByTagName("FatherPhoneHome").item(0).getTextContent();
        student.FatherPhoneCell = studentElement.getElementsByTagName("FatherPhoneCell").item(0).getTextContent();
        student.FatherPhoneWork = studentElement.getElementsByTagName("FatherPhoneWork").item(0).getTextContent();
        student.FatherPhoneExtn = studentElement.getElementsByTagName("FatherPhoneExtn").item(0).getTextContent();
        student.FatherOccupation = studentElement.getElementsByTagName("FatherOccupation").item(0).getTextContent();
        student.FatherWorkAddress = studentElement.getElementsByTagName("FatherWorkAddress").item(0).getTextContent();
        student.FatherNotes = studentElement.getElementsByTagName("FatherNotes").item(0).getTextContent();

        student.EmergencyName = studentElement.getElementsByTagName("EmergencyName").item(0).getTextContent();
        student.EmergencyPhoneHome = studentElement.getElementsByTagName("EmergencyPhoneHome").item(0).getTextContent();
        student.EmergencyPhoneCell = studentElement.getElementsByTagName("EmergencyPhoneCell").item(0).getTextContent();
        student.EmergencyPhoneWork = studentElement.getElementsByTagName("EmergencyPhoneWork").item(0).getTextContent();
        student.EmergencyPhoneExtn = studentElement.getElementsByTagName("EmergencyPhoneExtn").item(0).getTextContent();
        student.EmergencyNotes = studentElement.getElementsByTagName("EmergencyNotes").item(0).getTextContent();

        results.Student = student;
        StudentDetailsResults = results;
    }

    public class StudentDetailsResults {
        public String AccessLevel;

        public String ErrorCode;

        public Student Student;

        public String apiversion;

        public String NumberRecords;
    }

    public class Student {
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
}
