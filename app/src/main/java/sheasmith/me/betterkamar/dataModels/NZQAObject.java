package sheasmith.me.betterkamar.dataModels;

public class NZQAObject
{
    private StudentOfficialResultsResults StudentOfficialResultsResults;

    public StudentOfficialResultsResults getStudentOfficialResultsResults ()
    {
        return StudentOfficialResultsResults;
    }

    public void setStudentOfficialResultsResults (StudentOfficialResultsResults StudentOfficialResultsResults)
    {
        this.StudentOfficialResultsResults = StudentOfficialResultsResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [StudentOfficialResultsResults = "+StudentOfficialResultsResults+"]";
    }

    public class StudentOfficialResultsResults
    {
        private String AccessLevel;

        private String NumberTypes;

        private Types Types;

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

        public String getNumberTypes ()
        {
            return NumberTypes;
        }

        public void setNumberTypes (String NumberTypes)
        {
            this.NumberTypes = NumberTypes;
        }

        public Types getTypes ()
        {
            return Types;
        }

        public void setTypes (Types Types)
        {
            this.Types = Types;
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
            return "ClassPojo [AccessLevel = "+AccessLevel+", NumberTypes = "+NumberTypes+", Types = "+Types+", ErrorCode = "+ErrorCode+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+"]";
        }
    }


    public class Types
    {
        private Type[] Type;

        public Type[] getType ()
        {
            return Type;
        }

        public void setType (Type[] Type)
        {
            this.Type = Type;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Type = "+Type+"]";
        }
    }

    public class Qualification
    {
        private String Year;

        private String Ref;

        private String index;

        private String Level;

        private String Endorse;

        private String Title;

        public String getYear ()
        {
            return Year;
        }

        public void setYear (String Year)
        {
            this.Year = Year;
        }

        public String getRef ()
        {
            return Ref;
        }

        public void setRef (String Ref)
        {
            this.Ref = Ref;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getLevel ()
        {
            return Level;
        }

        public void setLevel (String Level)
        {
            this.Level = Level;
        }

        public String getEndorse ()
        {
            return Endorse;
        }

        public void setEndorse (String Endorse)
        {
            this.Endorse = Endorse;
        }

        public String getTitle ()
        {
            return Title;
        }

        public void setTitle (String Title)
        {
            this.Title = Title;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Year = "+Year+", Ref = "+Ref+", index = "+index+", Level = "+Level+", Endorse = "+Endorse+", Title = "+Title+"]";
        }
    }

    public class Type
    {
        private String index;

        private String TypeCode;

        private String NumberQuals;

        private Qualifications Qualifications;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getTypeCode ()
        {
            return TypeCode;
        }

        public void setTypeCode (String TypeCode)
        {
            this.TypeCode = TypeCode;
        }

        public String getNumberQuals ()
        {
            return NumberQuals;
        }

        public void setNumberQuals (String NumberQuals)
        {
            this.NumberQuals = NumberQuals;
        }

        public Qualifications getQualifications ()
        {
            return Qualifications;
        }

        public void setQualifications (Qualifications Qualifications)
        {
            this.Qualifications = Qualifications;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", TypeCode = "+TypeCode+", NumberQuals = "+NumberQuals+", Qualifications = "+Qualifications+"]";
        }
    }

    public class Qualifications
    {
        private Qualification[] Qualification;

        public Qualification[] getQualification ()
        {
            return Qualification;
        }

        public void setQualification (Qualification[] Qualification)
        {
            this.Qualification = Qualification;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Qualification = "+Qualification+"]";
        }
    }
}