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
}
