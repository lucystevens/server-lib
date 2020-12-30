package uk.co.lukestevens.server.setup;

import java.io.File;

public class ServerSetup {
	
	int port = 8000;
	boolean useDatabaseLogging = false;
	File configFile;
	
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean useDatabaseLogging() {
		return useDatabaseLogging;
	}
	
	public void useDatabaseLogging(boolean useDatabaseLogging) {
		this.useDatabaseLogging = useDatabaseLogging;
	}
	
	public boolean hasConfigFile() {
		return configFile != null;
	}
	
	public File getConfigFile() {
		return configFile;
	}
	
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
	
	

}
