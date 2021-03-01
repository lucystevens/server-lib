package uk.co.lukestevens.server.routes;

import java.util.List;

/**
 * An interface to return a list of defined http routes for a specific application
 * 
 * @author Luke Stevens
 */
public interface RouteConfiguration {
	
	/**
	 * Configures a list of routes to be served by this applications
	 * HTTP server.
	 * @return A list of routes
	 */
	List<DefinedRoute> configureRoutes();

}
