package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TheDiamondPicks on 8/09/2018.
 */

public class DetailsObject {
    private StudentDetailsResults StudentDetailsResults;

    public DetailsObject(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("StudentTimetableResults").item(0);
        Element studentElement = (Element) root.getElementsByTagName("StudentDetailsResults").item(0).getFirstChild();

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
        private String AccessLevel;

        private String ErrorCode;

        private Student Student;

        private String apiversion;

        private String NumberRecords;
    }

    public class Student {
        private String ParentSalutation;

        private String HealthNotes;

        private String EmergencyName;

        private String HomePhoneB;

        private String LastName;

        private String HomePhone;

        private String ParentTitleB;

        private String StudentID;

        private String SpecialCircumstances;

        private String ForeNames;

        private String NSN;

        private String MotherPhoneHome;

        private String Medical;

        private String ParentEmailB;

        private String MotherPhoneWork;

        private String EmergencyPhoneWork;

        private String EmergencyNotes;

        private String HomeAddressB;

        private String index;

        private String DentistName;

        private String FatherEmail;

        private String GeneralNotes;

        private String Reactions;

        private String FatherName;

        private String MotherName;

        private String MotherPhoneExtn;

        private String FatherWorkAddress;

        private String HealthFlag;

        private String FatherPhoneHome;

        private String FatherPhoneExtn;

        private String LastNameLegal;

        private String DentistAddress;

        private String Gender;

        private String EmergencyPhoneCell;

        private String EmergencyPhoneHome;

        private String ForeNamesLegal;

        private String EmergencyPhoneExtn;

        private String FatherOccupation;

        private String Age;

        private String ParentSalutationB;

        private String FatherRelation;

        private String Vaccinations;

        private String StudentEmail;

        private String FatherNotes;

        private String DentistPhone;

        private String ParentTitle;

        private String FirstNameLegal;

        private String DoctorPhone;

        private String DoctorAddress;

        private String FirstName;

        private String FatherPhoneWork;

        private String MotherOccupation;

        private String MotherRelation;

        private String HomeAddress;

        private String MotherNotes;

        private String Ethnicity;

        private String ParentEmail;

        private String AllowedIbuprofen;

        private String DoctorName;

        private String MotherEmail;

        private String StudentSchoolEmail;

        private String FatherPhoneCell;

        private String MotherPhoneCell;

        private String DateBirth;

        private String MotherWorkAddress;

        private String AllowedPanadol;

        private String FatherStatus;

        private String MotherStatus;
    }
}
