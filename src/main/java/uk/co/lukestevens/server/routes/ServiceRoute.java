package uk.co.lukestevens.server.routes;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.co.lukestevens.server.exceptions.ServerException;

/**
 * A functional interface to represent an API route<br>
 * Note this is very similar to {@link Route} but only
 * allows {@link ServerException} to be thrown.
 * 
 * @author luke.stevens
 */
@FunctionalInterface
public interface ServiceRoute {
	// TODO I don't think this class is actually needed; Route throws Exceptions too
	Object apply(Request request, Response response) throws ServerException;
}
