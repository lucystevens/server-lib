package uk.co.lukestevens.server.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ServerExceptionTest {
	
	@Test
	public void testBuildServerException_invalidRequest() {
		ServerException exception = ServerException.invalidRequest().build();
		assertEquals(200, exception.getHttpCode());
	}
	
	@Test
	public void testBuildServerException_badRequest() {
		ServerException exception = ServerException.badRequest().build();
		assertEquals(400, exception.getHttpCode());
	}
	
	@Test
	public void testBuildServerException_unauthorized() {
		ServerException exception = ServerException.unauthorized().build();
		assertEquals(401, exception.getHttpCode());
	}
	
	@Test
	public void testBuildServerException_forbidden() {
		ServerException exception = ServerException.forbidden().build();
		assertEquals(403, exception.getHttpCode());
	}
	
	@Test
	public void testBuildServerException_notFound() {
		ServerException exception = ServerException.notFound().build();
		assertEquals(404, exception.getHttpCode());
	}
	
	@Test
	public void testBuildServerException_withError() {
		ServerException exception = ServerException.invalidRequest()
				.withError("input", "Input too long.")
				.withError("image", "Image is not a png.")
				.build();
		
		assertEquals(2, exception.getErrors().size());
		assertEquals("Input too long.", exception.getErrors().get("input"));
		assertEquals("Image is not a png.", exception.getErrors().get("image"));
	}
	
	@Test
	public void testBuildServerException_withErrors() {
		Map<String, String> errors = new HashMap<>();
		errors.put("input", "Input too long.");
		errors.put("image", "Image is not a png.");
		
		ServerException exception = ServerException.invalidRequest()
				.withErrors(errors)
				.build();
		
		assertEquals(errors, exception.getErrors());
	}
	
	@Test
	public void testBuildServerException_withLogging() {
		ServerException exception = ServerException.invalidRequest()
				.logMessage("Database table does not exist.")
				.build();
		
		assertTrue(exception.shouldLog());
		assertEquals("Database table does not exist.", exception.getMessage());
	}
	
	@Test
	public void testThrowServerException() {
		ServerExceptionBuilder builder = ServerException.invalidRequest()
				.withError("input", "Input too long.")
				.withError("image", "Image is not a png.")
				.logMessage("Database table does not exist.");
		
		ServerException thrown = assertThrows(ServerException.class, builder::throwException);
		assertEquals("Database table does not exist.", thrown.getMessage());
		
	}

}
