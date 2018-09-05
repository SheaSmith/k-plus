package sheasmith.me.betterkamar.models;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class AbesenceObject
{
    private StudentAbsenceStatsResults StudentAbsenceStatsResults;

    public StudentAbsenceStatsResults getStudentAbsenceStatsResults ()
    {
        return StudentAbsenceStatsResults;
    }

    public void setStudentAbsenceStatsResults (StudentAbsenceStatsResults StudentAbsenceStatsResults)
    {
        this.StudentAbsenceStatsResults = StudentAbsenceStatsResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [StudentAbsenceStatsResults = "+StudentAbsenceStatsResults+"]";
    }

    public class StudentAbsenceStatsResults
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
        private String FullDaysO;

        private String index;

        private String HalfDaysT;

        private String HalfDaysU;

        private String FullDaysU;

        private String PctgeJ;

        private String FullDaysT;

        private String HalfDaysJ;

        private String HalfDaysO;

        private String FullDaysJ;

        private String HalfDaysOpen;

        private String FullDaysOpen;

        private String PctgeT;

        private String PctgeU;

        private String PctgeO;

        private String PctgeP;

        public String getFullDaysO ()
        {
            return FullDaysO;
        }

        public void setFullDaysO (String FullDaysO)
        {
            this.FullDaysO = FullDaysO;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getHalfDaysT ()
        {
            return HalfDaysT;
        }

        public void setHalfDaysT (String HalfDaysT)
        {
            this.HalfDaysT = HalfDaysT;
        }

        public String getHalfDaysU ()
        {
            return HalfDaysU;
        }

        public void setHalfDaysU (String HalfDaysU)
        {
            this.HalfDaysU = HalfDaysU;
        }

        public String getFullDaysU ()
        {
            return FullDaysU;
        }

        public void setFullDaysU (String FullDaysU)
        {
            this.FullDaysU = FullDaysU;
        }

        public String getPctgeJ ()
        {
            return PctgeJ;
        }

        public void setPctgeJ (String PctgeJ)
        {
            this.PctgeJ = PctgeJ;
        }

        public String getFullDaysT ()
        {
            return FullDaysT;
        }

        public void setFullDaysT (String FullDaysT)
        {
            this.FullDaysT = FullDaysT;
        }

        public String getHalfDaysJ ()
        {
            return HalfDaysJ;
        }

        public void setHalfDaysJ (String HalfDaysJ)
        {
            this.HalfDaysJ = HalfDaysJ;
        }

        public String getHalfDaysO ()
        {
            return HalfDaysO;
        }

        public void setHalfDaysO (String HalfDaysO)
        {
            this.HalfDaysO = HalfDaysO;
        }

        public String getFullDaysJ ()
        {
            return FullDaysJ;
        }

        public void setFullDaysJ (String FullDaysJ)
        {
            this.FullDaysJ = FullDaysJ;
        }

        public String getHalfDaysOpen ()
        {
            return HalfDaysOpen;
        }

        public void setHalfDaysOpen (String HalfDaysOpen)
        {
            this.HalfDaysOpen = HalfDaysOpen;
        }

        public String getFullDaysOpen ()
        {
            return FullDaysOpen;
        }

        public void setFullDaysOpen (String FullDaysOpen)
        {
            this.FullDaysOpen = FullDaysOpen;
        }

        public String getPctgeT ()
        {
            return PctgeT;
        }

        public void setPctgeT (String PctgeT)
        {
            this.PctgeT = PctgeT;
        }

        public String getPctgeU ()
        {
            return PctgeU;
        }

        public void setPctgeU (String PctgeU)
        {
            this.PctgeU = PctgeU;
        }

        public String getPctgeO ()
        {
            return PctgeO;
        }

        public void setPctgeO (String PctgeO)
        {
            this.PctgeO = PctgeO;
        }

        public String getPctgeP ()
        {
            return PctgeP;
        }

        public void setPctgeP (String PctgeP)
        {
            this.PctgeP = PctgeP;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [FullDaysO = "+FullDaysO+", index = "+index+", HalfDaysT = "+HalfDaysT+", HalfDaysU = "+HalfDaysU+", FullDaysU = "+FullDaysU+", PctgeJ = "+PctgeJ+", FullDaysT = "+FullDaysT+", HalfDaysJ = "+HalfDaysJ+", HalfDaysO = "+HalfDaysO+", FullDaysJ = "+FullDaysJ+", HalfDaysOpen = "+HalfDaysOpen+", FullDaysOpen = "+FullDaysOpen+", PctgeT = "+PctgeT+", PctgeU = "+PctgeU+", PctgeO = "+PctgeO+", PctgeP = "+PctgeP+"]";
        }
    }
}
