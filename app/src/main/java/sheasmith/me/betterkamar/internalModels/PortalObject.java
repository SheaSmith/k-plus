/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:53 PM
 */

package sheasmith.me.betterkamar.internalModels;

import java.io.Serializable;

/**
 * Created by shea9 on 18/02/2017.
 */
public class PortalObject implements Serializable {
    public String username;

    public String password;

    public String hostname;

    public String schoolName;

    public String student;

    public String studentFile;

    public String schoolFile;

    public String key;

    public String toString() {
        return this.student + " - " + this.schoolName;
    }
}
