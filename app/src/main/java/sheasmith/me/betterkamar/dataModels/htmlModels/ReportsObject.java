package sheasmith.me.betterkamar.dataModels.htmlModels;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Shea on 20/02/2017.
 */

public class ReportsObject implements Serializable {
    public String title;
    public String date;
    public String url;
    public Map<String, String> cookies;
}
