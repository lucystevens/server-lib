package uk.co.lukestevens.server.setup;

import java.io.File;


/**
 * Simple class for storing flags for setting up the application services
 * 
 * @author Luke Stevens
 */
public class ServerSetup {
	
	private int port = 8000;
	private boolean useDatabaseLogging = false;
	private File configFile;
	
	/**
	 * @return The port this application will run on. Defaults to 8000
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @param port The port this application will run on. Defaults to 8000
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @return Whether this application should log to database. Defaults to false.
	 */
	public boolean useDatabaseLogging() {
		return useDatabaseLogging;
	}
	
	/**
	 * @param useDatabaseLogging Whether this application should log to database. Defaults to false.
	 */
	public void useDatabaseLogging(boolean useDatabaseLogging) {
		this.useDatabaseLogging = useDatabaseLogging;
	}
	
	/**
	 * @return Whether this application should use a set of configs
	 * from a local file.
	 */
	public boolean hasConfigFile() {
		return configFile != null;
	}
	
	/**
	 * @return The local config file to load from
	 */
	public File getConfigFile() {
		return configFile;
	}
	
	/**
	 * @param configFile The local config file to load from
	 */
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
	
	

}
