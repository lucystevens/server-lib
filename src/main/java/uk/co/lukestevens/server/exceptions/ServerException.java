package uk.co.lukestevens.server.exceptions;

import java.util.Map;

/**
 * An exception class used to represent errors thrown from
 * API methods
 * 
 * @author luke.stevens
 */
public class ServerException extends Exception {

	private static final long serialVersionUID = 5690456456223971520L;
	
	/**
	 * Creates a 400 bad request error:<br>
	 * The server cannot or will not process the request
	 * due to an apparent client error (e.g., malformed request
	 * syntax, size too large, invalid request message framing,
	 * or deceptive request routing).
	 */
	public static ServerExceptionBuilder badRequest() {
		return new ServerExceptionBuilder(400);
	}
	
	
	/**
	 * Creates a 401 Unauthorized error:<br>
	 * Similar to <i>403 Forbidden</i>, but specifically for
	 * use when authentication is required and has failed or
	 * has not yet been provided.
	 */
	public static ServerExceptionBuilder unauthorized() {
		return new ServerExceptionBuilder(401);
	}
	
	
	/**
	 * Creates a 403 forbidden error:<br>
	 * The request was valid, but the server is refusing action.
	 * The user might not have the necessary permissions for a
	 * resource, or may need an account of some sort.
	 */
	public static ServerExceptionBuilder forbidden() {
		return new ServerExceptionBuilder(403);
	}
	
	
	/**
	 * Creates a 404 not found error:<br>
	 * The origin server did not find a current representation
	 * for the target resource or is not willing to disclose that one exists.
	 */
	public static ServerExceptionBuilder notFound() {
		return new ServerExceptionBuilder(404);
	}
	
	
	private final int httpCode;
	private final Map<String, String> errors;
	private final boolean shouldLog;
	
	// Constructor for client only error
	ServerException(int httpCode, Map<String, String> errors) {
		this.shouldLog = false;
		this.httpCode = httpCode;
		this.errors = errors;
	}
	
	// Constructor for client errors with server side logging
	ServerException(int httpCode, Map<String, String> errors, String logMessage) {
		super(logMessage);
		this.shouldLog = true;
		this.httpCode = httpCode;
		this.errors = errors;
	}

	/**
	 * @return The HTTP status code
	 */
	public int getHttpCode() {
		return httpCode;
	}

	/**
	 * @return A map of errors to be returned to the client
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @return Whether a message should be logged by the server
	 */
	public boolean shouldLog() {
		return shouldLog;
	}
	
	

}
