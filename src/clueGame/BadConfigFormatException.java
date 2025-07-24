package clueGame;

import java.io.*;

/**
 * Exception thrown when a configuration file is formatted incorrectly.
 * Used to signal errors during parsing of game setup or layout files.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: July 21, 2025
 * 
 * Used Oracle docs to help with PrintWriter class
 */

public class BadConfigFormatException extends Exception{
	
	private static final String LOG_FILE = "log.txt";
	
	public BadConfigFormatException() {
		super();
		logError("Bad config file format.");
	}
	
	
	public BadConfigFormatException(String message) {
		super(message);
		logError(message);
	}
	
	private void logError(String message) {
		try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
			out.println("CONFIG ERROR: " + message);
		} catch (IOException e) {
			System.err.println("ERROR - Could not write to log file: " + LOG_FILE);
		}
	}
}
