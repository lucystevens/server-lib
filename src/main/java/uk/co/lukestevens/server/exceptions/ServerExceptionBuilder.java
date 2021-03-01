package uk.co.lukestevens.server.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder class for creating server exceptions more easily
 * 
 * @author Luke Stevens
 */
public class ServerExceptionBuilder {
	
	private final int httpCode;
	private String logMessage;
	private final Map<String, String> errors = new HashMap<>();
	
	/**
	 * Start building a new server exception with a http status code
	 * @param httpCode The status code for the exception
	 */
	ServerExceptionBuilder(int httpCode) {
		this.httpCode = httpCode;
	}
	
	/**
	 * Add a default error to be returned to the client by this exception under the key 'error'
	 * @param message The error message to be displayed under the error key
	 * @return This builder instance
	 */
	public ServerExceptionBuilder withError(String message) {
		return this.withError("error", message);
	}

	/**
	 * Add an error to be returned to the client by this exception
	 * @param error The error key to be returned
	 * @param message The error message to be displayed under the above key
	 * @return This builder instance
	 */
	public ServerExceptionBuilder withError(String error, String message) {
		this.errors.put(error, message);
		return this;
	}
	
	/**
	 * Adds several errors to be returned to the client by this exception
	 * @param errors A map of errors to be returned
	 * @return This builder instance
	 */
	public ServerExceptionBuilder withErrors(Map<String, String> errors) {
		this.errors.putAll(errors);
		return this;
	}
	
	/**
	 * Adds a message to be logged on the server side. This is where sensitive data should be added
	 * @param logMessage The message to log
	 * @return This builder instance
	 */
	public ServerExceptionBuilder logMessage(String logMessage) {
		this.logMessage = logMessage;
		return this;
	}
	
	/**
	 * Uses the data in this builder to construct a new ServerException
	 * @return A new ServerException
	 */
	public ServerException build() {
		return logMessage == null? 
				new ServerException(httpCode, errors) : 
				new ServerException(httpCode, errors, logMessage);
	}
	
	/**
	 * Uses the data in this builder to construct a new ServerException and throws it
	 * @throws ServerException
	 */
	public void throwException() throws ServerException {
		throw this.build();
	}

}
