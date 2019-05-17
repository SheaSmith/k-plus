/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 23/09/18 1:18 PM
 */

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
