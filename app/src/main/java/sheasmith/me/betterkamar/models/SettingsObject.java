package sheasmith.me.betterkamar.models;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class SettingsObject
{
    private SettingsResults SettingsResults;

    public SettingsResults getSettingsResults ()
    {
        return SettingsResults;
    }

    public void setSettingsResults (SettingsResults SettingsResults)
    {
        this.SettingsResults = SettingsResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SettingsResults = "+SettingsResults+"]";
    }

    public class SettingsResults
    {
        private String AccessLevel;

        private String AssessmentTypesShown;

        private String MinAndroidVersion;

        private String StaffAllowed;

        private String StaffSavedPasswords;

        private String SchoolName;

        private String StudentsAllowed;

        private String apiversion;

        private String MiniOSVersion;

        private String LogoPath;

        private String StudentsSavedPasswords;

        private String SettingsVersion;

        private String ShowEnrolledEntries;

        private String ErrorCode;

        private UserAccess UserAccess;

        public String getAccessLevel ()
        {
            return AccessLevel;
        }

        public void setAccessLevel (String AccessLevel)
        {
            this.AccessLevel = AccessLevel;
        }

        public String getAssessmentTypesShown ()
        {
            return AssessmentTypesShown;
        }

        public void setAssessmentTypesShown (String AssessmentTypesShown)
        {
            this.AssessmentTypesShown = AssessmentTypesShown;
        }

        public String getMinAndroidVersion ()
        {
            return MinAndroidVersion;
        }

        public void setMinAndroidVersion (String MinAndroidVersion)
        {
            this.MinAndroidVersion = MinAndroidVersion;
        }

        public String getStaffAllowed ()
        {
            return StaffAllowed;
        }

        public void setStaffAllowed (String StaffAllowed)
        {
            this.StaffAllowed = StaffAllowed;
        }

        public String getStaffSavedPasswords ()
        {
            return StaffSavedPasswords;
        }

        public void setStaffSavedPasswords (String StaffSavedPasswords)
        {
            this.StaffSavedPasswords = StaffSavedPasswords;
        }

        public String getSchoolName ()
        {
            return SchoolName;
        }

        public void setSchoolName (String SchoolName)
        {
            this.SchoolName = SchoolName;
        }

        public String getStudentsAllowed ()
        {
            return StudentsAllowed;
        }

        public void setStudentsAllowed (String StudentsAllowed)
        {
            this.StudentsAllowed = StudentsAllowed;
        }

        public String getApiversion ()
        {
            return apiversion;
        }

        public void setApiversion (String apiversion)
        {
            this.apiversion = apiversion;
        }

        public String getMiniOSVersion ()
        {
            return MiniOSVersion;
        }

        public void setMiniOSVersion (String MiniOSVersion)
        {
            this.MiniOSVersion = MiniOSVersion;
        }

        public String getLogoPath ()
        {
            return LogoPath;
        }

        public void setLogoPath (String LogoPath)
        {
            this.LogoPath = LogoPath;
        }

        public String getStudentsSavedPasswords ()
        {
            return StudentsSavedPasswords;
        }

        public void setStudentsSavedPasswords (String StudentsSavedPasswords)
        {
            this.StudentsSavedPasswords = StudentsSavedPasswords;
        }

        public String getSettingsVersion ()
        {
            return SettingsVersion;
        }

        public void setSettingsVersion (String SettingsVersion)
        {
            this.SettingsVersion = SettingsVersion;
        }

        public String getShowEnrolledEntries ()
        {
            return ShowEnrolledEntries;
        }

        public void setShowEnrolledEntries (String ShowEnrolledEntries)
        {
            this.ShowEnrolledEntries = ShowEnrolledEntries;
        }

        public String getErrorCode ()
        {
            return ErrorCode;
        }

        public void setErrorCode (String ErrorCode)
        {
            this.ErrorCode = ErrorCode;
        }

        public UserAccess getUserAccess ()
        {
            return UserAccess;
        }

        public void setUserAccess (UserAccess UserAccess)
        {
            this.UserAccess = UserAccess;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [AccessLevel = "+AccessLevel+", AssessmentTypesShown = "+AssessmentTypesShown+", MinAndroidVersion = "+MinAndroidVersion+", StaffAllowed = "+StaffAllowed+", StaffSavedPasswords = "+StaffSavedPasswords+", SchoolName = "+SchoolName+", StudentsAllowed = "+StudentsAllowed+", apiversion = "+apiversion+", MiniOSVersion = "+MiniOSVersion+", LogoPath = "+LogoPath+", StudentsSavedPasswords = "+StudentsSavedPasswords+", SettingsVersion = "+SettingsVersion+", ShowEnrolledEntries = "+ShowEnrolledEntries+", ErrorCode = "+ErrorCode+", UserAccess = "+UserAccess+"]";
        }
    }
    public class UserAccess
    {
        private User[] User;

        private CalendarSettings CalendarSettings;

        public User[] getUser ()
        {
            return User;
        }

        public void setUser (User[] User)
        {
            this.User = User;
        }

        public CalendarSettings getCalendarSettings ()
        {
            return CalendarSettings;
        }

        public void setCalendarSettings (CalendarSettings CalendarSettings)
        {
            this.CalendarSettings = CalendarSettings;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [User = "+User+", CalendarSettings = "+CalendarSettings+"]";
        }
    }
    public class CalendarSettings
    {
        private String orange;

        private String auburn;

        private String red;

        private String purple;

        private String aero;

        private String blue;

        private String green;

        private String tangerine;

        private String violet;

        private String teal;

        private String black;

        private String amazon;

        public String getOrange ()
        {
            return orange;
        }

        public void setOrange (String orange)
        {
            this.orange = orange;
        }

        public String getAuburn ()
        {
            return auburn;
        }

        public void setAuburn (String auburn)
        {
            this.auburn = auburn;
        }

        public String getRed ()
        {
            return red;
        }

        public void setRed (String red)
        {
            this.red = red;
        }

        public String getPurple ()
        {
            return purple;
        }

        public void setPurple (String purple)
        {
            this.purple = purple;
        }

        public String getAero ()
        {
            return aero;
        }

        public void setAero (String aero)
        {
            this.aero = aero;
        }

        public String getBlue ()
        {
            return blue;
        }

        public void setBlue (String blue)
        {
            this.blue = blue;
        }

        public String getGreen ()
        {
            return green;
        }

        public void setGreen (String green)
        {
            this.green = green;
        }

        public String getTangerine ()
        {
            return tangerine;
        }

        public void setTangerine (String tangerine)
        {
            this.tangerine = tangerine;
        }

        public String getViolet ()
        {
            return violet;
        }

        public void setViolet (String violet)
        {
            this.violet = violet;
        }

        public String getTeal ()
        {
            return teal;
        }

        public void setTeal (String teal)
        {
            this.teal = teal;
        }

        public String getBlack ()
        {
            return black;
        }

        public void setBlack (String black)
        {
            this.black = black;
        }

        public String getAmazon ()
        {
            return amazon;
        }

        public void setAmazon (String amazon)
        {
            this.amazon = amazon;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [orange = "+orange+", auburn = "+auburn+", red = "+red+", purple = "+purple+", aero = "+aero+", blue = "+blue+", green = "+green+", tangerine = "+tangerine+", violet = "+violet+", teal = "+teal+", black = "+black+", amazon = "+amazon+"]";
        }
    }
    public class User
    {
        private String index;

        private String Notices;

        private String Events;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getNotices ()
        {
            return Notices;
        }

        public void setNotices (String Notices)
        {
            this.Notices = Notices;
        }

        public String getEvents ()
        {
            return Events;
        }

        public void setEvents (String Events)
        {
            this.Events = Events;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", Notices = "+Notices+", Events = "+Events+"]";
        }
    }

}
