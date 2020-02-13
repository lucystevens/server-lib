package uk.co.lukestevens.server;


import java.util.Collections;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.RouteImpl;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggerFactory;
import uk.co.lukestevens.server.exceptions.ServerException;

/**
 * A custom route wrapper to handle gson parsing
 * and ServerException logging
 * 
 * @author luke.stevens
 */
public class RouteWrapper extends RouteImpl {
	
	private final Logger logger;
	private final Gson gson = new Gson();
	private final ServiceRoute route;

	/**
	 * Create a new route wrapper
	 * @param path The path to this route
	 * @param route The API method for this route
	 * @param loggerFactory A LoggerFactory to get the correct logger for this class
	 */
	protected RouteWrapper(String path, ServiceRoute route, LoggerFactory loggerFactory) {
		super(path);
		this.route = route;
		this.logger = loggerFactory.getLogger(RouteWrapper.class);
	}

	@Override
	public Object handle(Request req, Response res) throws Exception {
		logger.debug("Request received from " + req.ip() + ": " + req.requestMethod() + " "   + req.url());
		
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
			
			// Return a Server response error
			ServerResponse error = ServerResponse.error(e.getErrors());
			return gson.toJson(error);
		} catch (Exception e) {
			
			// Catch and log unexpected errors
			logger.error(e);
			
			// Return a default Server response error
			ServerResponse error = ServerResponse.error(Collections.singletonMap("error", "Something went wrong, try again later"));
			return gson.toJson(error);
		}
	}

}
