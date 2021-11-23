package uk.co.lukestevens.server.routes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;
import spark.route.HttpMethod;
import uk.co.lukestevens.server.ServerResponse;
import uk.co.lukestevens.server.exceptions.ServerException;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.util.Collections;

/**
 * An extension to the base route configuration class that provides some
 * default logic for json serialisation and exception handling in routes.</br>
 * This can be accessed by manually wrapping logic using the {@link #handleRoute(Route)}
 * method, or using one of the ease-of-use http methods.
 * 
 * @author Luke Stevens
 *
 */
public abstract class AbstractRouteConfiguration implements RouteConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(RouteConfiguration.class);
	public static final Object EMPTY_RESPONSE = new JsonObject();
	
	private static final String LOGGING_TEMPLATE = "Request received from %s: %s %s";
	private static final String DEFAULT_ERROR_RESPONSE = "Something went wrong, try again later";

	protected final Gson gson;

	/**
	 * Setup default route handling logic for route configuration
	 * @param gson The gson instance to use for serialising route responses
	 */
	@Inject
	public AbstractRouteConfiguration(Gson gson) {
		this.gson = gson;
	}

	/**
	 * Wraps a simple Route to convert it to the format required by Spark.</br>
	 * This also provides json serialisation for responses, and exception handling for the route,
	 * returning a response with a standard format {@link ServerResponse} body.
	 * @param route The simple Route to wrap
	 * @return A Spark-compatible route
	 */
	public Route handleRoute(Route route) {
		return (req, res) -> {
			logger.info(String.format(LOGGING_TEMPLATE, req.ip(), req.requestMethod(), req.url()));
			
			try {
				
				// If method successful, convert response to JSON
				Object response = route.handle(req, res);
				ServerResponse success = ServerResponse.success(response);
				return gson.toJson(success);
				
			} catch (ServerException e) {
				
				// Otherwise log if necessary
				if(e.shouldLog()) {
					logger.error("Server error encountered", e);
				}
				
				res.status(e.getHttpCode());
				
				// Return a Server response error
				ServerResponse error = ServerResponse.error(e.getErrors());
				return gson.toJson(error);
			} catch (Exception e) {
				
				// Catch and log unexpected errors
				logger.error("Unexpected error encountered", e);
				
				res.status(HttpURLConnection.HTTP_BAD_REQUEST);
				
				// Return a default Server response error
				ServerResponse error = ServerResponse.error(
						Collections.singletonMap("error", DEFAULT_ERROR_RESPONSE));
				return gson.toJson(error);
			}
		};
	}
	
	/**
	 * Create a new defined GET route
	 * @param path The route path which is used for matching.
	 * @param route The simple Route implementation. This will be wrapped
	 * by the {@link #handleRoute(Route)} logic.
	 * @return A DefinedRoute, ready to be added to the server
	 */
	protected DefinedRoute GET(String path, Route route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.get);
	}
	
	/**
	 * Create a new defined POST route
	 * @param path The route path which is used for matching.
	 * @param route The simple Route implementation. This will be wrapped
	 * by the {@link #handleRoute(Route)} logic.
	 * @return A DefinedRoute, ready to be added to the server
	 */
	protected DefinedRoute POST(String path, Route route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.post);
	}
	
	/**
	 * Create a new defined PUT route
	 * @param path The route path which is used for matching.
	 * @param route The simple Route implementation. This will be wrapped
	 * by the {@link #handleRoute(Route)} logic.
	 * @return A DefinedRoute, ready to be added to the server
	 */
	protected DefinedRoute PUT(String path, Route route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.put);
	}
	
	/**
	 * Create a new defined DELETE route
	 * @param path The route path which is used for matching.
	 * @param route The simple Route implementation. This will be wrapped
	 * by the {@link #handleRoute(Route)} logic.
	 * @return A DefinedRoute, ready to be added to the server
	 */
	protected DefinedRoute DELETE(String path, Route route) {
		return new DefinedRoute(path, handleRoute(route), HttpMethod.delete);
	}

}
