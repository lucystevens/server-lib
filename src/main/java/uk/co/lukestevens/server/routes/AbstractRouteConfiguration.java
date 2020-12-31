package uk.co.lukestevens.server.routes;

import java.util.Collections;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import spark.Route;
import spark.route.HttpMethod;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.server.ServerResponse;
import uk.co.lukestevens.server.exceptions.ServerException;

public abstract class AbstractRouteConfiguration implements RouteConfiguration {
	
	public static final Object EMPTY_RESPONSE = new JsonObject();
	
	private static final String LOGGING_TEMPLATE = "Request received from %s: %s %s";
	private static final String DEFAULT_ERROR_RESPONSE = "Something went wrong, try again later";
	
	protected final Logger logger;
	protected final Gson gson;

	/**
	 * Setup default route handling logic for route configuration
	 * @param loggerFactory A LoggerFactory to get the correct logger for this class
	 * @param gson The gson instance to use for serialising route responses
	 */
	@Inject
	public AbstractRouteConfiguration(LoggingProvider loggingProvider, Gson gson) {
		this.gson = gson;
		this.logger = loggingProvider.getLogger(RouteConfiguration.class);
	}

	public Route handleRoute(ServiceRoute route) {
		return (req, res) -> {
			logger.debug(String.format(LOGGING_TEMPLATE, req.ip(), req.requestMethod(), req.url()));
			
			try {
				
				// If method successful, convert response to JSON
				Object response = route.apply(req, res);
				ServerResponse success = ServerResponse.success(response);
				return gson.toJson(success);
				
			} catch (ServerException e) {
				
				// Otherwise log if necessary
				if(e.shouldLog()) {
					logger.error(e);
				}
				
				res.status(e.getHttpCode());
				
				// Return a Server response error
				ServerResponse error = ServerResponse.error(e.getErrors());
				return gson.toJson(error);
			} catch (Exception e) {
				
				// Catch and log unexpected errors
				logger.error(e);
				
				res.status(400);
				
				// Return a default Server response error
				ServerResponse error = ServerResponse.error(
						Collections.singletonMap("error", DEFAULT_ERROR_RESPONSE));
				return gson.toJson(error);
			}
		};
	}
	
	protected DefinedRoute GET(String path, ServiceRoute route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.get);
	}
	
	protected DefinedRoute POST(String path, ServiceRoute route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.post);
	}
	
	protected DefinedRoute PUT(String path, ServiceRoute route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.put);
	}
	
	protected DefinedRoute DELETE(String path, ServiceRoute route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.delete);
	}

}
