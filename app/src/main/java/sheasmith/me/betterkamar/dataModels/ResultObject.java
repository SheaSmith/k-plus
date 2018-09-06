package sheasmith.me.betterkamar.dataModels;

public class ResultObject
{
    private StudentResultsResults StudentResultsResults;

    public StudentResultsResults getStudentResultsResults ()
    {
        return StudentResultsResults;
    }

    public void setStudentResultsResults (StudentResultsResults StudentResultsResults)
    {
        this.StudentResultsResults = StudentResultsResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [StudentResultsResults = "+StudentResultsResults+"]";
    }

    public class StudentResultsResults
    {
        private String AccessLevel;

        private String NumberResultLevels;

        private String ErrorCode;

        private ResultLevels ResultLevels;

        private String apiversion;

        private String NumberRecords;

        private String StudentID;

        public String getAccessLevel ()
        {
            return AccessLevel;
        }

        public void setAccessLevel (String AccessLevel)
        {
            this.AccessLevel = AccessLevel;
        }

        public String getNumberResultLevels ()
        {
            return NumberResultLevels;
        }

        public void setNumberResultLevels (String NumberResultLevels)
        {
            this.NumberResultLevels = NumberResultLevels;
        }

        public String getErrorCode ()
        {
            return ErrorCode;
        }

        public void setErrorCode (String ErrorCode)
        {
            this.ErrorCode = ErrorCode;
        }

        public ResultLevels getResultLevels ()
        {
            return ResultLevels;
        }

        public void setResultLevels (ResultLevels ResultLevels)
        {
            this.ResultLevels = ResultLevels;
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

        public String getStudentID ()
        {
            return StudentID;
        }

        public void setStudentID (String StudentID)
        {
            this.StudentID = StudentID;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [AccessLevel = "+AccessLevel+", NumberResultLevels = "+NumberResultLevels+", ErrorCode = "+ErrorCode+", ResultLevels = "+ResultLevels+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+", StudentID = "+StudentID+"]";
        }
    }

    public class ResultLevels
    {
        private ResultLevel[] ResultLevel;

        public ResultLevel[] getResultLevel ()
        {
            return ResultLevel;
        }

        public void setResultLevel (ResultLevel[] ResultLevel)
        {
            this.ResultLevel = ResultLevel;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [ResultLevel = "+ResultLevel+"]";
        }
    }
    public class Result
    {
        private String SubField;

        private String CreditsPassed;

        private String index;

        private String Grade;

        private String Credits;

        private String Number;

        private String ResultPublished;

        private String Version;

        private String Title;

        public String getSubField ()
        {
            return SubField;
        }

        public void setSubField (String SubField)
        {
            this.SubField = SubField;
        }

        public String getCreditsPassed ()
        {
            return CreditsPassed;
        }

        public void setCreditsPassed (String CreditsPassed)
        {
            this.CreditsPassed = CreditsPassed;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getGrade ()
        {
            return Grade;
        }

        public void setGrade (String Grade)
        {
            this.Grade = Grade;
        }

        public String getCredits ()
        {
            return Credits;
        }

        public void setCredits (String Credits)
        {
            this.Credits = Credits;
        }

        public String getNumber ()
        {
            return Number;
        }

        public void setNumber (String Number)
        {
            this.Number = Number;
        }

        public String getResultPublished ()
        {
            return ResultPublished;
        }

        public void setResultPublished (String ResultPublished)
        {
            this.ResultPublished = ResultPublished;
        }

        public String getVersion ()
        {
            return Version;
        }

        public void setVersion (String Version)
        {
            this.Version = Version;
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
            return "ClassPojo [SubField = "+SubField+", CreditsPassed = "+CreditsPassed+", index = "+index+", Grade = "+Grade+", Credits = "+Credits+", Number = "+Number+", ResultPublished = "+ResultPublished+", Version = "+Version+", Title = "+Title+"]";
        }
    }

    public class ResultLevel
    {
        private String NCEALevel;

        private String index;

        private String NumberResults;

        private Results Results;

        public String getNCEALevel ()
        {
            return NCEALevel;
        }

        public void setNCEALevel (String NCEALevel)
        {
            this.NCEALevel = NCEALevel;
        }

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getNumberResults ()
        {
            return NumberResults;
        }

        public void setNumberResults (String NumberResults)
        {
            this.NumberResults = NumberResults;
        }

        public Results getResults ()
        {
            return Results;
        }

        public void setResults (Results Results)
        {
            this.Results = Results;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [NCEALevel = "+NCEALevel+", index = "+index+", NumberResults = "+NumberResults+", Results = "+Results+"]";
        }
    }

    public class Results
    {
        private Result Result;

        public Result getResult ()
        {
            return Result;
        }

        public void setResult (Result Result)
        {
            this.Result = Result;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Result = "+Result+"]";
        }
    }
}