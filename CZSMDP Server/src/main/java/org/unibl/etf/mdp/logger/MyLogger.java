package org.unibl.etf.mdp.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
	private static Logger logger;
	private static String LOG_FILE = "logging.txt";
	
	public static void setup() throws IOException
	{
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.INFO);
		FileHandler logFile = new FileHandler(LOG_FILE, true);
		
		SimpleFormatter formatter = new SimpleFormatter();
		logFile.setFormatter(formatter);
		logger.addHandler(logFile);
	}
	
	
	  public static void log(Level level,String poruka, Exception e)
	    {
	        logger.log(level, poruka, e);
	    }
}
