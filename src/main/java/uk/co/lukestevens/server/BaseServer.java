package uk.co.lukestevens.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;
import spark.RouteImpl;
import spark.Service;
import spark.route.HttpMethod;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.injection.AppPort;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggingProvider;

/**
 * A base server class to be used to create Spark services
 * on the development machine.
 * 
 * 
 * @author luke.stevens
 */
@Singleton
public class BaseServer {
	
	private final LoggingProvider loggingProvider;
	private final Logger logger;
	private final Gson gson;
	private final ApplicationProperties appProps;

	final Service service;	
	
	/**
	 * Create a new base server configuration based off an application model
	 * @param application The application model for this application
	 * @param version The version of the service running
	 * @param loggerFactory A LoggerFactory to get the correct logger for this class
	 * @param gson The gson instance to use for serialising route responses
	 */
	@Inject
	public BaseServer(@AppPort Integer port, LoggingProvider loggingProvider, ApplicationProperties appProps, Gson gson) {
		this.loggingProvider = loggingProvider;
		this.gson = gson;
		this.logger = loggingProvider.getLogger(BaseServer.class);
		this.appProps = appProps;
		
		this.service = Service.ignite();
		this.service.port(port);
	}
	
	/**	
	 * Adds a route to the primary service
	 * @param method The HTTP method for this route
	 * @param path The path to access this route
	 * @param route The method to call when the route is accessed
	 */
	public void addRoute(HttpMethod method, String path, ServiceRoute route) {
		RouteImpl wrappedRoute = new RouteWrapper(path, route, loggingProvider, gson);
		this.service.addRoute(method, wrappedRoute);
	}
	
	/**
	 * Enables Cross-Origin Resource Sharing for this
	 * service
	 */
	public void enableCORS() {
		this.enableCORS(service);
	}
	
	/**
	 * Enables Cross-Origin Resource Sharing for a service
	 */
	void enableCORS(Service service) {
		service.after((request, response) -> {
        	response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        	response.header("Access-Control-Allow-Origin", "*");
        	response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        	response.header("Access-Control-Allow-Credentials", "true");
        });
	}
	
	/**
	 * @return The service
	 */
	public Service getService() {
		return this.service;
	}
	
	/**
	 * Initialises the services
	 */
	public void init() {
		this.service.get("/api/status", this::status);
		this.service.awaitInitialization();
		logger.info("Service started on port " + this.service.port());
	}
	
	/*
	 * Handles the status request for this server
	 */
	Object status(Request req, Response res) {
		JsonObject json = new JsonObject();
		json.addProperty("name", this.appProps.getApplicationName());
		json.addProperty("version", this.appProps.getApplicationVersion());
		return json.toString();
	}
	
	/**
	 * Shuts down both services, and the application
	 */
	public void shutdown() {
		this.service.awaitStop();
		System.exit(0);
	}

}
