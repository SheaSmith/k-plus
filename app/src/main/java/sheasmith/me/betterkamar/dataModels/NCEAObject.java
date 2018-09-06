package sheasmith.me.betterkamar.dataModels;

public class NCEAObject
{
    private StudentNCEASummaryResults StudentNCEASummaryResults;

    public StudentNCEASummaryResults getStudentNCEASummaryResults ()
    {
        return StudentNCEASummaryResults;
    }

    public void setStudentNCEASummaryResults (StudentNCEASummaryResults StudentNCEASummaryResults)
    {
        this.StudentNCEASummaryResults = StudentNCEASummaryResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [StudentNCEASummaryResults = "+StudentNCEASummaryResults+"]";
    }

    public class StudentNCEASummaryResults
    {
        private String AccessLevel;

        private String ErrorCode;

        private Students Students;

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

        public String getErrorCode ()
        {
            return ErrorCode;
        }

        public void setErrorCode (String ErrorCode)
        {
            this.ErrorCode = ErrorCode;
        }

        public Students getStudents ()
        {
            return Students;
        }

        public void setStudents (Students Students)
        {
            this.Students = Students;
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
            return "ClassPojo [AccessLevel = "+AccessLevel+", ErrorCode = "+ErrorCode+", Students = "+Students+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+"]";
        }
    }

    public class Students
    {
        private Student Student;

        public Student getStudent ()
        {
            return Student;
        }

        public void setStudent (Student Student)
        {
            this.Student = Student;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Student = "+Student+"]";
        }
    }

    public class Student
    {
        private String index;

        private CreditsExternal CreditsExternal;

        private YearTotals YearTotals;

        private NCEA NCEA;

        private LevelTotals LevelTotals;

        private CreditsTotal CreditsTotal;

        private CreditsInternal CreditsInternal;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public CreditsExternal getCreditsExternal ()
        {
            return CreditsExternal;
        }

        public void setCreditsExternal (CreditsExternal CreditsExternal)
        {
            this.CreditsExternal = CreditsExternal;
        }

        public YearTotals getYearTotals ()
        {
            return YearTotals;
        }

        public void setYearTotals (YearTotals YearTotals)
        {
            this.YearTotals = YearTotals;
        }

        public NCEA getNCEA ()
        {
            return NCEA;
        }

        public void setNCEA (NCEA NCEA)
        {
            this.NCEA = NCEA;
        }

        public LevelTotals getLevelTotals ()
        {
            return LevelTotals;
        }

        public void setLevelTotals (LevelTotals LevelTotals)
        {
            this.LevelTotals = LevelTotals;
        }

        public CreditsTotal getCreditsTotal ()
        {
            return CreditsTotal;
        }

        public void setCreditsTotal (CreditsTotal CreditsTotal)
        {
            this.CreditsTotal = CreditsTotal;
        }

        public CreditsInternal getCreditsInternal ()
        {
            return CreditsInternal;
        }

        public void setCreditsInternal (CreditsInternal CreditsInternal)
        {
            this.CreditsInternal = CreditsInternal;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", CreditsExternal = "+CreditsExternal+", YearTotals = "+YearTotals+", NCEA = "+NCEA+", LevelTotals = "+LevelTotals+", CreditsTotal = "+CreditsTotal+", CreditsInternal = "+CreditsInternal+"]";
        }
    }

    public class CreditsInternal
    {
        private String Achieved;

        private String Merit;

        private String Excellence;

        private String Total;

        private String Attempted;

        public String getAchieved ()
        {
            return Achieved;
        }

        public void setAchieved (String Achieved)
        {
            this.Achieved = Achieved;
        }

        public String getMerit ()
        {
            return Merit;
        }

        public void setMerit (String Merit)
        {
            this.Merit = Merit;
        }

        public String getExcellence ()
        {
            return Excellence;
        }

        public void setExcellence (String Excellence)
        {
            this.Excellence = Excellence;
        }

        public String getTotal ()
        {
            return Total;
        }

        public void setTotal (String Total)
        {
            this.Total = Total;
        }

        public String getAttempted ()
        {
            return Attempted;
        }

        public void setAttempted (String Attempted)
        {
            this.Attempted = Attempted;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Achieved = "+Achieved+", Merit = "+Merit+", Excellence = "+Excellence+", Total = "+Total+", Attempted = "+Attempted+"]";
        }
    }

    public class CreditsTotal
    {
        private String Achieved;

        private String Merit;

        private String Excellence;

        private String Total;

        private String Attempted;

        public String getAchieved ()
        {
            return Achieved;
        }

        public void setAchieved (String Achieved)
        {
            this.Achieved = Achieved;
        }

        public String getMerit ()
        {
            return Merit;
        }

        public void setMerit (String Merit)
        {
            this.Merit = Merit;
        }

        public String getExcellence ()
        {
            return Excellence;
        }

        public void setExcellence (String Excellence)
        {
            this.Excellence = Excellence;
        }

        public String getTotal ()
        {
            return Total;
        }

        public void setTotal (String Total)
        {
            this.Total = Total;
        }

        public String getAttempted ()
        {
            return Attempted;
        }

        public void setAttempted (String Attempted)
        {
            this.Attempted = Attempted;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Achieved = "+Achieved+", Merit = "+Merit+", Excellence = "+Excellence+", Total = "+Total+", Attempted = "+Attempted+"]";
        }
    }

    public class LevelTotals
    {
        private LevelTotal[] LevelTotal;

        public LevelTotal[] getLevelTotal ()
        {
            return LevelTotal;
        }

        public void setLevelTotal (LevelTotal[] LevelTotal)
        {
            this.LevelTotal = LevelTotal;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [LevelTotal = "+LevelTotal+"]";
        }
    }

    public class LevelTotal
    {
        private String index;

        private String Achieved;

        private String Merit;

        private String Excellence;

        private String Level;

        private String Total;

        private String Attempted;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getAchieved ()
        {
            return Achieved;
        }

        public void setAchieved (String Achieved)
        {
            this.Achieved = Achieved;
        }

        public String getMerit ()
        {
            return Merit;
        }

        public void setMerit (String Merit)
        {
            this.Merit = Merit;
        }

        public String getExcellence ()
        {
            return Excellence;
        }

        public void setExcellence (String Excellence)
        {
            this.Excellence = Excellence;
        }

        public String getLevel ()
        {
            return Level;
        }

        public void setLevel (String Level)
        {
            this.Level = Level;
        }

        public String getTotal ()
        {
            return Total;
        }

        public void setTotal (String Total)
        {
            this.Total = Total;
        }

        public String getAttempted ()
        {
            return Attempted;
        }

        public void setAttempted (String Attempted)
        {
            this.Attempted = Attempted;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", Achieved = "+Achieved+", Merit = "+Merit+", Excellence = "+Excellence+", Level = "+Level+", Total = "+Total+", Attempted = "+Attempted+"]";
        }
    }

    public class NCEA
    {
        private String NCEAL1LIT;

        private String L2NCEA;

        private String L3NCEA;

        private String L1NCEA;

        private String NCEANUM;

        private String NCEAUELIT;

        public String getNCEAL1LIT ()
        {
            return NCEAL1LIT;
        }

        public void setNCEAL1LIT (String NCEAL1LIT)
        {
            this.NCEAL1LIT = NCEAL1LIT;
        }

        public String getL2NCEA ()
        {
            return L2NCEA;
        }

        public void setL2NCEA (String L2NCEA)
        {
            this.L2NCEA = L2NCEA;
        }

        public String getL3NCEA ()
        {
            return L3NCEA;
        }

        public void setL3NCEA (String L3NCEA)
        {
            this.L3NCEA = L3NCEA;
        }

        public String getL1NCEA ()
        {
            return L1NCEA;
        }

        public void setL1NCEA (String L1NCEA)
        {
            this.L1NCEA = L1NCEA;
        }

        public String getNCEANUM ()
        {
            return NCEANUM;
        }

        public void setNCEANUM (String NCEANUM)
        {
            this.NCEANUM = NCEANUM;
        }

        public String getNCEAUELIT ()
        {
            return NCEAUELIT;
        }

        public void setNCEAUELIT (String NCEAUELIT)
        {
            this.NCEAUELIT = NCEAUELIT;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [NCEAL1LIT = "+NCEAL1LIT+", L2NCEA = "+L2NCEA+", L3NCEA = "+L3NCEA+", L1NCEA = "+L1NCEA+", NCEANUM = "+NCEANUM+", NCEAUELIT = "+NCEAUELIT+"]";
        }
    }

    public class YearTotals
    {
        private YearTotal[] YearTotal;

        public YearTotal[] getYearTotal ()
        {
            return YearTotal;
        }

        public void setYearTotal (YearTotal[] YearTotal)
        {
            this.YearTotal = YearTotal;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [YearTotal = "+YearTotal+"]";
        }
    }

    public class YearTotal
    {
        private String Year;

        private String index;

        private String Merit;

        private String Excellence;

        private String Total;

        private String Attempted;

        public String getYear ()
        {
            return Year;
        }

        public void setYear (String Year)
        {
            this.Year = Year;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getMerit ()
        {
            return Merit;
        }

        public void setMerit (String Merit)
        {
            this.Merit = Merit;
        }

        public String getExcellence ()
        {
            return Excellence;
        }

        public void setExcellence (String Excellence)
        {
            this.Excellence = Excellence;
        }

        public String getTotal ()
        {
            return Total;
        }

        public void setTotal (String Total)
        {
            this.Total = Total;
        }

        public String getAttempted ()
        {
            return Attempted;
        }

        public void setAttempted (String Attempted)
        {
            this.Attempted = Attempted;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Year = "+Year+", index = "+index+", Merit = "+Merit+", Excellence = "+Excellence+", Total = "+Total+", Attempted = "+Attempted+"]";
        }
    }

    public class CreditsExternal
    {
        private String Achieved;

        private String Merit;

        private String Excellence;

        private String Total;

        private String Attempted;

        public String getAchieved ()
        {
            return Achieved;
        }

        public void setAchieved (String Achieved)
        {
            this.Achieved = Achieved;
        }

        public String getMerit ()
        {
            return Merit;
        }

        public void setMerit (String Merit)
        {
            this.Merit = Merit;
        }

        public String getExcellence ()
        {
            return Excellence;
        }

        public void setExcellence (String Excellence)
        {
            this.Excellence = Excellence;
        }

        public String getTotal ()
        {
            return Total;
        }

        public void setTotal (String Total)
        {
            this.Total = Total;
        }

        public String getAttempted ()
        {
            return Attempted;
        }

        public void setAttempted (String Attempted)
        {
            this.Attempted = Attempted;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Achieved = "+Achieved+", Merit = "+Merit+", Excellence = "+Excellence+", Total = "+Total+", Attempted = "+Attempted+"]";
        }
    }
}
