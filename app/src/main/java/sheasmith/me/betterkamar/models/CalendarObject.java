package sheasmith.me.betterkamar.models;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class CalendarObject
{
    private EventsResults EventsResults;

    public EventsResults getEventsResults ()
    {
        return EventsResults;
    }

    public void setEventsResults (EventsResults EventsResults)
    {
        this.EventsResults = EventsResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [EventsResults = "+EventsResults+"]";
    }

    public class EventsResults
    {
        private String AccessLevel;

        private Events Events;

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

        public Events getEvents ()
        {
            return Events;
        }

        public void setEvents (Events Events)
        {
            this.Events = Events;
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
            return "ClassPojo [AccessLevel = "+AccessLevel+", Events = "+Events+", ErrorCode = "+ErrorCode+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+"]";
        }
    }

    public class Events
    {
        private Event[] Event;

        public Event[] getEvent ()
        {
            return Event;
        }

        public void setEvent (Event[] Event)
        {
            this.Event = Event;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Event = "+Event+"]";
        }
    }

    public class Event
    {
        private String index;

        private String Staff;

        private String Start;

        private String Details;

        private String DateTimeStart;

        private String Finish;

        private String Location;

        private String Title;

        private String Priority;

        private String Student;

        private String CG1;

        private String Colour;

        private String ColourLabel;

        private String CG2;

        private String DateTimeFinish;

        private String DateTimeInfo;

        private String Public;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getStaff ()
        {
            return Staff;
        }

        public void setStaff (String Staff)
        {
            this.Staff = Staff;
        }

        public String getStart ()
        {
            return Start;
        }

        public void setStart (String Start)
        {
            this.Start = Start;
        }

        public String getDetails ()
        {
            return Details;
        }

        public void setDetails (String Details)
        {
            this.Details = Details;
        }

        public String getDateTimeStart ()
        {
            return DateTimeStart;
        }

        public void setDateTimeStart (String DateTimeStart)
        {
            this.DateTimeStart = DateTimeStart;
        }

        public String getFinish ()
        {
            return Finish;
        }

        public void setFinish (String Finish)
        {
            this.Finish = Finish;
        }

        public String getLocation ()
        {
            return Location;
        }

        public void setLocation (String Location)
        {
            this.Location = Location;
        }

        public String getTitle ()
        {
            return Title;
        }

        public void setTitle (String Title)
        {
            this.Title = Title;
        }

        public String getPriority ()
        {
            return Priority;
        }

        public void setPriority (String Priority)
        {
            this.Priority = Priority;
        }

        public String getStudent ()
        {
            return Student;
        }

        public void setStudent (String Student)
        {
            this.Student = Student;
        }

        public String getCG1 ()
        {
            return CG1;
        }

        public void setCG1 (String CG1)
        {
            this.CG1 = CG1;
        }

        public String getColour ()
        {
            return Colour;
        }

        public void setColour (String Colour)
        {
            this.Colour = Colour;
        }

        public String getColourLabel ()
        {
            return ColourLabel;
        }

        public void setColourLabel (String ColourLabel)
        {
            this.ColourLabel = ColourLabel;
        }

        public String getCG2 ()
        {
            return CG2;
        }

        public void setCG2 (String CG2)
        {
            this.CG2 = CG2;
        }

        public String getDateTimeFinish ()
        {
            return DateTimeFinish;
        }

        public void setDateTimeFinish (String DateTimeFinish)
        {
            this.DateTimeFinish = DateTimeFinish;
        }

        public String getDateTimeInfo ()
        {
            return DateTimeInfo;
        }

        public void setDateTimeInfo (String DateTimeInfo)
        {
            this.DateTimeInfo = DateTimeInfo;
        }

        public String getPublic ()
        {
            return Public;
        }

        public void setPublic (String Public)
        {
            this.Public = Public;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", Staff = "+Staff+", Start = "+Start+", Details = "+Details+", DateTimeStart = "+DateTimeStart+", Finish = "+Finish+", Location = "+Location+", Title = "+Title+", Priority = "+Priority+", Student = "+Student+", CG1 = "+CG1+", Colour = "+Colour+", ColourLabel = "+ColourLabel+", CG2 = "+CG2+", DateTimeFinish = "+DateTimeFinish+", DateTimeInfo = "+DateTimeInfo+", Public = "+Public+"]";
        }
    }
}

