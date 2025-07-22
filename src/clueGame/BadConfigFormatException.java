package clueGame;

/**
 * Exception thrown when a configuration file is formatted incorrectly.
 * Used to signal errors during parsing of game setup or layout files.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: July 21, 2025
 */

public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super();
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}
