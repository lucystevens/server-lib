package uk.co.lukestevens.server;

import java.util.Map;

/**
 * A class to represent a simple server response
 * 
 * @author luke.stevens
 */
public class ServerResponse {
	
	/**
	 * Create a new successful server response
	 * @param data The data payload of the response
	 * @return A successful ServerResponse
	 */
	public static ServerResponse success(Object data) {
		return new ServerResponse(data);
	}
	
	/**
	 * Create an errored server response
	 * @param errors A Map of keys to errors representing the failure
	 * @return An errored ServerResponse
	 */
	public static ServerResponse error(Map<String, String> errors) {
		return new ServerResponse(errors);
	}
	
	private boolean success;
	private Map<String, String> errors;
	private Object data;
	
	// Private constructor for errored response
	private ServerResponse(Map<String, String> errors) {
		this.success = false;
		this.errors = errors;
	}
	
	// Private constructor for success response
	private ServerResponse(Object data) {
		this.success = true;
		this.data = data;
	}

	/**
	 * @return If this response is successful
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return A map of errors if this response errored, otherwise null
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * @return The response payload if successful, otherwise null
	 */
	public Object getData() {
		return data;
	}

}
