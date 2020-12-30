package uk.co.lukestevens.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gson.JsonObject;

import spark.Request;
import spark.Response;
import spark.Service;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.injection.AppPort;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.server.routes.RouteConfiguration;

/**
 * A base server class to be used to create Spark services
 * on the development machine.
 * 
 * 
 * @author luke.stevens
 */
@Singleton
public class BaseServer {
	
	private final Logger logger;
	private final ApplicationProperties appProps;
	private final RouteConfiguration routeConfig;

	final Service service;	
	
	@Inject
	public BaseServer(@AppPort Integer port, LoggingProvider loggingProvider, ApplicationProperties appProps, RouteConfiguration routeConfig) {
		this.logger = loggingProvider.getLogger(BaseServer.class);
		this.appProps = appProps;
		this.routeConfig = routeConfig;
		
		this.service = Service.ignite();
		this.service.port(port);
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
		// Add all routes
		this.routeConfig
			.configureRoutes()
			.forEach(route -> this.service.addRoute(route.getMethod(), route));
		
		// Add default status route
		this.service.get("/api/status", this::status);
		this.service.awaitInitialization();
		logger.info("Service started on port " + this.service.port());
	}
	
	/*
	 * Handles the status request for this server
	 */
	protected Object status(Request req, Response res) {
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
