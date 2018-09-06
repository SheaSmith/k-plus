package sheasmith.me.betterkamar.dataModels;

public class AttendanceObject
{
    private StudentAttendanceResults StudentAttendanceResults;

    public StudentAttendanceResults getStudentAttendanceResults ()
    {
        return StudentAttendanceResults;
    }

    public void setStudentAttendanceResults (StudentAttendanceResults StudentAttendanceResults)
    {
        this.StudentAttendanceResults = StudentAttendanceResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [StudentAttendanceResults = "+StudentAttendanceResults+"]";
    }

    public class StudentAttendanceResults
    {
        private String AccessLevel;

        private Weeks Weeks;

        private String ErrorCode;

        private String apiversion;

        private String NumberRecords;

        public String getAccessLevel ()
        {
            return AccessLevel;
        }

        public void setAccessLevel (String AccessLevel)
        {
            this.AccessLevel = AccessLevel;
        }

        public Weeks getWeeks ()
        {
            return Weeks;
        }

        public void setWeeks (Weeks Weeks)
        {
            this.Weeks = Weeks;
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

        @Override
        public String toString()
        {
            return "ClassPojo [AccessLevel = "+AccessLevel+", Weeks = "+Weeks+", ErrorCode = "+ErrorCode+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+"]";
        }
    }

    public class Weeks
    {
        private Week[] Week;

        public Week[] getWeek ()
        {
            return Week;
        }

        public void setWeek (Week[] Week)
        {
            this.Week = Week;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Week = "+Week+"]";
        }
    }

    public class Day
    {
        private String content;

        private String index;

        public String getContent ()
        {
            return content;
        }

        public void setContent (String content)
        {
            this.content = content;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [content = "+content+", index = "+index+"]";
        }
    }

    public class Week
    {
        private String index;

        private String WeekStart;

        private Days Days;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getWeekStart ()
        {
            return WeekStart;
        }

        public void setWeekStart (String WeekStart)
        {
            this.WeekStart = WeekStart;
        }

        public Days getDays ()
        {
            return Days;
        }

        public void setDays (Days Days)
        {
            this.Days = Days;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", WeekStart = "+WeekStart+", Days = "+Days+"]";
        }
    }

    public class Days
    {
        private Day2[] Day;

        public Day2[] getDay ()
        {
            return Day;
        }

        public void setDay (Day2[] Day)
        {
            this.Day = Day;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Day = "+Day+"]";
        }
    }

    public class Day2
    {
        private String index;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+"]";
        }
    }


}