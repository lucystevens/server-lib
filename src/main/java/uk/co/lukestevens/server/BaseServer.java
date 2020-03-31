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
import uk.co.lukestevens.config.annotations.AppVersion;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerFactory;
import uk.co.lukestevens.server.models.APIApplication;

/**
 * A base server class to be used to create Spark services
 * on the development machine.
 * 
 * 
 * @author luke.stevens
 */
@Singleton
public class BaseServer {
	
	private final LoggerFactory loggerFactory;
	private final Logger logger;
	private final Gson gson;

	final Service primaryService;
	final Service internalService;
	final String version;
	
	final APIApplication application;
	
	
	/**
	 * Create a new base server configuration based off an application model
	 * @param application The application model for this application
	 * @param version The version of the service running
	 * @param loggerFactory A LoggerFactory to get the correct logger for this class
	 * @param gson The gson instance to use for serialising route responses
	 */
	@Inject
	public BaseServer(APIApplication application, @AppVersion String version, LoggerFactory loggerFactory, Gson gson) {
		this.version = version;
		this.loggerFactory = loggerFactory;
		this.gson = gson;
		this.logger = loggerFactory.getLogger(BaseServer.class);
		this.application = application;
		
		this.primaryService = Service.ignite();
		this.primaryService.port(application.getRunningPort());
		
		this.internalService = Service.ignite();
		this.internalService.port(application.getInternalPort());
		this.enableCORS(internalService);
		this.internalService.get("/shutdown", (req, res) -> {this.shutdown(); return "Server did not shutdown";});
	}
	
	/**
	 * Adds a route to the primary service
	 * @param method The HTTP method for this route
	 * @param path The path to access this route
	 * @param route The method to call when the route is accessed
	 */
	public void addRoute(HttpMethod method, String path, ServiceRoute route) {
		RouteImpl wrappedRoute = new RouteWrapper(path, route, loggerFactory, gson);
		this.primaryService.addRoute(method, wrappedRoute);
	}
	
	/**
	 * Enables Cross-Origin Resource Sharing for the
	 * primary service
	 */
	public void enableCORS() {
		this.enableCORS(primaryService);
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
	 * @return The primary service
	 */
	public Service getService() {
		return this.primaryService;
	}
	
	/**
	 * Initialises both services
	 */
	public void init() {
		this.primaryService.get("/api/status", this::status);
		this.primaryService.awaitInitialization();
		this.internalService.awaitInitialization();
		logger.info("Service started on port " + this.primaryService.port());
	}
	
	/*
	 * Handles the status request for this server
	 */
	Object status(Request req, Response res) {
		JsonObject json = new JsonObject();
		json.addProperty("name", this.application.getName());
		json.addProperty("version", this.version);
		return json.toString();
	}
	
	/**
	 * Shuts down both services, and the application
	 */
	public void shutdown() {
		this.primaryService.awaitStop();
		this.internalService.awaitStop();
		System.exit(0);
	}

}
