package uk.co.lukestevens.server.routes;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteImpl;
import spark.route.HttpMethod;

/**
 * An extension of RouteImpl that also defines the HttpMethod, allowing
 * it to be easily added to the service without any additional information
 * 
 * @author luke.stevens
 */
public class DefinedRoute extends RouteImpl {
	
	protected final Route route;
	protected final HttpMethod method;
	
	/**
	 * Creates a new DefinedRoute
	 * @param path The route path which is used for matching. (e.g. /hello, users/:name)
	 * @param route The route function to be called at this route
	 * @param method The HttpMethod for this route.
	 */
	public DefinedRoute(String path, Route route, HttpMethod method) {
		super(path);
		this.route = route;
		this.method = method;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception {
		return route.handle(req, res);
	}
	
	/**
	 * @return The defined Http method for this route
	 */
	public HttpMethod getMethod() {
		return method;
	}

}
