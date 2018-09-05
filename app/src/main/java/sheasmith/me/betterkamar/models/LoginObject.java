package sheasmith.me.betterkamar.models;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class LoginObject
{
    private LogonResults LogonResults;

    public LogonResults getLogonResults ()
    {
        return LogonResults;
    }

    public void setLogonResults (LogonResults LogonResults)
    {
        this.LogonResults = LogonResults;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LogonResults = "+LogonResults+"]";
    }

    public class LogonResults
    {
        private String AccessLevel;

        private String Key;

        private String LogonLevel;

        private String ErrorCode;

        private String Success;

        private String CurrentStudent;

        private String apiversion;

        public String getAccessLevel ()
        {
            return AccessLevel;
        }

        public void setAccessLevel (String AccessLevel)
        {
            this.AccessLevel = AccessLevel;
        }

        public String getKey ()
        {
            return Key;
        }

        public void setKey (String Key)
        {
            this.Key = Key;
        }

        public String getLogonLevel ()
        {
            return LogonLevel;
        }

        public void setLogonLevel (String LogonLevel)
        {
            this.LogonLevel = LogonLevel;
        }

        public String getErrorCode ()
        {
            return ErrorCode;
        }

        public void setErrorCode (String ErrorCode)
        {
            this.ErrorCode = ErrorCode;
        }

        public String getSuccess ()
        {
            return Success;
        }

        public void setSuccess (String Success)
        {
            this.Success = Success;
        }

        public String getCurrentStudent ()
        {
            return CurrentStudent;
        }

        public void setCurrentStudent (String CurrentStudent)
        {
            this.CurrentStudent = CurrentStudent;
        }

        public String getApiversion ()
        {
            return apiversion;
        }

        public void setApiversion (String apiversion)
        {
            this.apiversion = apiversion;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [AccessLevel = "+AccessLevel+", Key = "+Key+", LogonLevel = "+LogonLevel+", ErrorCode = "+ErrorCode+", Success = "+Success+", CurrentStudent = "+CurrentStudent+", apiversion = "+apiversion+"]";
        }
    }
}