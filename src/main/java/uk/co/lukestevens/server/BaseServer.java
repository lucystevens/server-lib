package uk.co.lukestevens.server;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.injection.annotations.AppPort;
import uk.co.lukestevens.server.routes.RouteConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * A base server class housing the main boilerplate for HTTP
 * services.
 * 
 * @author luke.stevens
 */
@Singleton
public class BaseServer {

	private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);


	private final ApplicationProperties appProps;
	private final RouteConfiguration routeConfig;

	private final Service service;	
	
	/**
	 * Creates and initialises a new BaseServer
	 * @param port The port to run the server on
	 * @param appProps The application properties used for application name and version
	 * @param routeConfig The specific routes to be served by this server
	 */
	@Inject
	public BaseServer(@AppPort Integer port, ApplicationProperties appProps, RouteConfiguration routeConfig) {
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
	 * @return The internal Spark service
	 */
	public Service getService() {
		return this.service;
	}
	
	/**
	 * Initialises the services using the injected RouteConfiguration
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
	
	/**
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
