package uk.co.lukestevens.server.models;

/**
 * An enum representing the types of application
 * 
 * @author luke.stevens
 */
public enum ApplicationType {
	
	/**
	 * An API application: an application that exposes services for
	 * websites across multiple domains to call
	 */
	API,
	
	/**
	 * A website application: a service that serves
	 * some static content 
	 */
	WEBSITE,
	
	/**
	 * A library: Some code that does not  run on it's own
	 * but provides common features for other applications
	 */
	LIBRARY,
	
	/**
	 * A desktop application: An application that is designed
	 * to be run from the desktop environment
	 */
	DESKTOP;

}
