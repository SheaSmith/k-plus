package sheasmith.me.betterkamar.dataModels;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class GlobalObject
{
    private GlobalsResults GlobalsResults;

    public GlobalsResults getGlobalsResults ()
    {
        return GlobalsResults;
    }

    public void setGlobalsResults (GlobalsResults GlobalsResults)
    {
        this.GlobalsResults = GlobalsResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [GlobalsResults = "+GlobalsResults+"]";
    }

    public class GlobalsResults
    {
        private String AccessLevel;

        private String ErrorCode;

        private PeriodDefinitions PeriodDefinitions;

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

        public PeriodDefinitions getPeriodDefinitions ()
        {
            return PeriodDefinitions;
        }

        public void setPeriodDefinitions (PeriodDefinitions PeriodDefinitions)
        {
            this.PeriodDefinitions = PeriodDefinitions;
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
            return "ClassPojo [AccessLevel = "+AccessLevel+", ErrorCode = "+ErrorCode+", PeriodDefinitions = "+PeriodDefinitions+", apiversion = "+apiversion+", NumberRecords = "+NumberRecords+"]";
        }
    }
    public class PeriodDefinitions
    {
        private PeriodDefinition[] PeriodDefinition;

        public PeriodDefinition[] getPeriodDefinition ()
        {
            return PeriodDefinition;
        }

        public void setPeriodDefinition (PeriodDefinition[] PeriodDefinition)
        {
            this.PeriodDefinition = PeriodDefinition;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [PeriodDefinition = "+PeriodDefinition+"]";
        }
    }
    public class PeriodDefinition
    {
        private String index;

        private String PeriodTime;

        private String PeriodName;

        public String getIndex ()
        {
            return index;
        }

        public void setIndex (String index)
        {
            this.index = index;
        }

        public String getPeriodTime ()
        {
            return PeriodTime;
        }

        public void setPeriodTime (String PeriodTime)
        {
            this.PeriodTime = PeriodTime;
        }

        public String getPeriodName ()
        {
            return PeriodName;
        }

        public void setPeriodName (String PeriodName)
        {
            this.PeriodName = PeriodName;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [index = "+index+", PeriodTime = "+PeriodTime+", PeriodName = "+PeriodName+"]";
        }
    }
}