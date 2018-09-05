package sheasmith.me.betterkamar.models;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class NoticesObject
{
    private NoticesResults NoticesResults;

    public NoticesResults getNoticesResults ()
    {
        return NoticesResults;
    }

    public void setNoticesResults (NoticesResults NoticesResults)
    {
        this.NoticesResults = NoticesResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [NoticesResults = "+NoticesResults+"]";
    }

    public class NoticesResults
    {
        private String AccessLevel;

        private GeneralNotices GeneralNotices;

        private String ErrorCode;

        private String apiversion;

        private String NumberRecords;

        private String NoticeDate;

        private MeetingNotices MeetingNotices;

        public String getAccessLevel ()
        {
            return AccessLevel;
        }

        public void setAccessLevel (String AccessLevel)
        {
            this.AccessLevel = AccessLevel;
        }

        public GeneralNotices getGeneralNotices ()
        {
            return GeneralNotices;
        }

        public void setGeneralNotices (GeneralNotices GeneralNotices)
        {
            this.GeneralNotices = GeneralNotices;
        }

        public String getErrorCode ()
        {
            return ErrorCode;
        }

        public void setErrorCode (String ErrorCode)
        {
            this.ErrorCode = ErrorCode;
        }

        public String getApiversion ()
        {
            return apiversion;
        }

        public void setApiversion (String apiversion)
        {
            this.apiversion = apiversion;
        }

        public String getNumberRecords ()
        {
            return NumberRecords;
        }

        public void setNumberRecords (String NumberRecords)
        {
            this.NumberRecords = NumberRecords;
        }

        public String getNoticeDate ()
        {
            return NoticeDate;
        }

        public void setNoticeDate (String NoticeDate)
        {
            this.NoticeDate = NoticeDate;
        }

        public MeetingNotices getMeetingNotices ()
        {
            return MeetingNotices;
        }

        public void setMeetingNotices (MeetingNotices MeetingNotices)
        {
            this.MeetingNotices = MeetingNotices;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [AccessLevel = "+AccessLevel+", GeneralNotices = "+GeneralNotices+", ErrorCode = "+ErrorCode+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+", NoticeDate = "+NoticeDate+", MeetingNotices = "+MeetingNotices+"]";
        }
    }

    public class MeetingNotices
    {
        private String NumberMeetingRecords;

        private Meeting[] Meeting;

        public String getNumberMeetingRecords ()
        {
            return NumberMeetingRecords;
        }

        public void setNumberMeetingRecords (String NumberMeetingRecords)
        {
            this.NumberMeetingRecords = NumberMeetingRecords;
        }

        public Meeting[] getMeeting ()
        {
            return Meeting;
        }

        public void setMeeting (Meeting[] Meeting)
        {
            this.Meeting = Meeting;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [NumberMeetingRecords = "+NumberMeetingRecords+", Meeting = "+Meeting+"]";
        }
    }

    public class Meeting
    {
        private String PlaceMeet;

        private String index;

        private String Body;

        private String Teacher;

        private String Subject;

        private String DateMeet;

        private String TimeMeet;

        private String Level;

        public String getPlaceMeet ()
        {
            return PlaceMeet;
        }

        public void setPlaceMeet (String PlaceMeet)
        {
            this.PlaceMeet = PlaceMeet;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getBody ()
        {
            return Body;
        }

        public void setBody (String Body)
        {
            this.Body = Body;
        }

        public String getTeacher ()
        {
            return Teacher;
        }

        public void setTeacher (String Teacher)
        {
            this.Teacher = Teacher;
        }

        public String getSubject ()
        {
            return Subject;
        }

        public void setSubject (String Subject)
        {
            this.Subject = Subject;
        }

        public String getDateMeet ()
        {
            return DateMeet;
        }

        public void setDateMeet (String DateMeet)
        {
            this.DateMeet = DateMeet;
        }

        public String getTimeMeet ()
        {
            return TimeMeet;
        }

        public void setTimeMeet (String TimeMeet)
        {
            this.TimeMeet = TimeMeet;
        }

        public String getLevel ()
        {
            return Level;
        }

        public void setLevel (String Level)
        {
            this.Level = Level;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [PlaceMeet = "+PlaceMeet+", index = "+index+", Body = "+Body+", Teacher = "+Teacher+", Subject = "+Subject+", DateMeet = "+DateMeet+", TimeMeet = "+TimeMeet+", Level = "+Level+"]";
        }
    }

    public class GeneralNotices
    {
        private General[] General;

        private String NumberGeneralRecords;

        public General[] getGeneral ()
        {
            return General;
        }

        public void setGeneral (General[] General)
        {
            this.General = General;
        }

        public String getNumberGeneralRecords ()
        {
            return NumberGeneralRecords;
        }

        public void setNumberGeneralRecords (String NumberGeneralRecords)
        {
            this.NumberGeneralRecords = NumberGeneralRecords;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [General = "+General+", NumberGeneralRecords = "+NumberGeneralRecords+"]";
        }
    }

    public class General
    {
        private String index;

        private String Body;

        private String Teacher;

        private String Subject;

        private String Level;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getBody ()
        {
            return Body;
        }

        public void setBody (String Body)
        {
            this.Body = Body;
        }

        public String getTeacher ()
        {
            return Teacher;
        }

        public void setTeacher (String Teacher)
        {
            this.Teacher = Teacher;
        }

        public String getSubject ()
        {
            return Subject;
        }

        public void setSubject (String Subject)
        {
            this.Subject = Subject;
        }

        public String getLevel ()
        {
            return Level;
        }

        public void setLevel (String Level)
        {
            this.Level = Level;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", Body = "+Body+", Teacher = "+Teacher+", Subject = "+Subject+", Level = "+Level+"]";
        }
    }
}