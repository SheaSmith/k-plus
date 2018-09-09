package sheasmith.me.betterkamar.dataModels;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import sheasmith.me.betterkamar.internalModels.Exceptions;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class NoticesObject
{
    public NoticesResults NoticesResults;

    public NoticesObject(String xml) throws IOException, SAXException, ParserConfigurationException, Exceptions.ExpiredToken, Exceptions.UnknownServerError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = (Element) doc.getElementsByTagName("NoticesResults").item(0);

        if (root.getElementsByTagName("NumberRecords").getLength() == 0) {
            String error = root.getElementsByTagName("Error").item(0).getTextContent();
            if (error.equalsIgnoreCase("invalid key")) {
                throw new Exceptions.ExpiredToken();
            } else {
                throw new Exceptions.UnknownServerError();
            }
        }

        Element meetingNoticesElement = (Element) root.getElementsByTagName("MeetingNotices");
        Element generalNoticesElement = (Element) root.getElementsByTagName("GeneralNotices");

        NoticesResults results = new NoticesResults();
        results.AccessLevel = root.getElementsByTagName("AccessLevel").item(0).getTextContent();
        results.ErrorCode = root.getElementsByTagName("ErrorCode").item(0).getTextContent();
        results.NoticeDate = root.getElementsByTagName("NoticeDate").item(0).getTextContent();
        results.NumberRecords = root.getElementsByTagName("NumberRecords").item(0).getTextContent();

        results.MeetingNotices = new MeetingNotices();
        results.MeetingNotices.NumberMeetingRecords = meetingNoticesElement.getElementsByTagName("NumberMeetingRecords").item(0).getTextContent();

        NodeList meetings = meetingNoticesElement.getElementsByTagName("Meeting");
        for (int i = 0; i != meetings.getLength(); i++) {
            Element meetingElement = (Element) meetings.item(0);
            Meeting meeting = new Meeting();

            meeting.Level = meetingElement.getElementsByTagName("Level").item(0).getTextContent();
            meeting.Subject = meetingElement.getElementsByTagName("Subject").item(0).getTextContent();
            meeting.Body = meetingElement.getElementsByTagName("Body").item(0).getTextContent();
            meeting.Teacher = meetingElement.getElementsByTagName("Teacher").item(0).getTextContent();
            meeting.PlaceMeet = meetingElement.getElementsByTagName("PlaceMeet").item(0).getTextContent();
            meeting.DateMeet = meetingElement.getElementsByTagName("DateMeet").item(0).getTextContent();
            meeting.TimeMeet = meetingElement.getElementsByTagName("TimeMeet").item(0).getTextContent();
            meeting.index = meetingElement.getAttribute("index");

            results.MeetingNotices.Meeting.add(meeting);
        }

        results.GeneralNotices = new GeneralNotices();
        results.GeneralNotices.NumberGeneralRecords = generalNoticesElement.getElementsByTagName("NumberGeneralRecords").item(0).getTextContent();

        NodeList generals = meetingNoticesElement.getElementsByTagName("General");
        for (int i = 0; i != generals.getLength(); i++) {
            Element generalElement = (Element) meetings.item(0);
            General general = new General();

            general.Level = generalElement.getElementsByTagName("Level").item(0).getTextContent();
            general.Subject = generalElement.getElementsByTagName("Subject").item(0).getTextContent();
            general.Body = generalElement.getElementsByTagName("Body").item(0).getTextContent();
            general.Teacher = generalElement.getElementsByTagName("Teacher").item(0).getTextContent();
            general.index = generalElement.getAttribute("index");

            results.GeneralNotices.General.add(general);
        }

        NoticesResults = results;
    }

    public class NoticesResults
    {
        public String AccessLevel;

        public GeneralNotices GeneralNotices;

        public String ErrorCode;

        public String apiversion;

        public String NumberRecords;

        public String NoticeDate;

        public MeetingNotices MeetingNotices;
    }

    public class MeetingNotices
    {
        public String NumberMeetingRecords;

        public List<Meeting> Meeting = new ArrayList<>();
    }

    public class Meeting
    {
        public String PlaceMeet;

        public String index;

        public String Body;

        public String Teacher;

        public String Subject;

        public String DateMeet;

        public String TimeMeet;

        public String Level;
    }

    public class GeneralNotices
    {
        public List<General> General = new ArrayList<>();

        public String NumberGeneralRecords;
    }

    public class General
    {
        public String index;

        public String Body;

        public String Teacher;

        public String Subject;

        public String Level;
    }
}