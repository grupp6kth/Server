package util;

/**
 * Simple static class for outputting regular and error messages to console
 */
public class OutputToConsole {
    /**
     * Prints regular message to console
     * @param message - String message
     */
    public static void printMessageToConsole(String message){
        System.out.println(message);
    }

    /**
     * Prints error message to console
     * @param message - String message
     */
    public static void printErrorMessageToConsole(String message){
        System.err.println(message);
    }
}
