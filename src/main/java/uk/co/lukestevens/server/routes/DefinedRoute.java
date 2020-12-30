package uk.co.lukestevens.server.routes;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.RouteImpl;
import spark.route.HttpMethod;

/**
 * An extension of RouteImpl that also defines the HttpMethod
 * 
 * @author luke.stevens
 */
public class DefinedRoute extends RouteImpl {
	
	protected final Route route;
	protected final HttpMethod method;
	
	public DefinedRoute(String path, Route route, HttpMethod method) {
		super(path);
		this.route = route;
		this.method = method;
	}

	@Override
	public Object handle(Request req, Response res) throws Exception {
		return route.handle(req, res);
	}
	
	public HttpMethod getMethod() {
		return method;
	}

}
