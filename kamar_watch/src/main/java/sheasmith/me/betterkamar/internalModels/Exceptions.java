/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.internalModels;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public class Exceptions {

    public static class InvalidUsernamePassword extends Exception {}
    public static class UnknownServerError extends Exception {}
    public static class InvalidServer extends Exception {}

    public static class InvalidToken extends Exception {
    }

    public static class ExpiredToken extends Exception {
    }
    public static class TooManyAttempts extends Exception {}

    public static class AccessDenied extends Exception {
    }
}
