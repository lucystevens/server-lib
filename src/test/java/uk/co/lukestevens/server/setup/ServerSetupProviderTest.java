package uk.co.lukestevens.server.setup;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.jupiter.api.Test;

public class ServerSetupProviderTest {
	
	@Test
	public void testParseCommandLine_allShortOpts() throws ParseException {
		String[] args = new String[] {
				"-c", "src/test/resources/conf/test.conf",
				"-d",
				"-p", "9003"
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 9003);
		assertTrue(setup.hasConfigFile());
		assertEquals("test.conf", setup.getConfigFile().getName());
		assertTrue(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_allLongOpts() throws ParseException {
		String[] args = new String[] {
				"--config", "src/test/resources/conf/test.conf",
				"--enable-db-logging",
				"--port", "9003"
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 9003);
		assertTrue(setup.hasConfigFile());
		assertEquals("test.conf", setup.getConfigFile().getName());
		assertTrue(setup.useDatabaseLogging());
	}

	@Test
	public void testParseCommandLine_withConfigFileShortOpt() throws ParseException {
		String[] args = new String[] {
				"-c", "src/test/resources/conf/test.conf",
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 8000);
		assertTrue(setup.hasConfigFile());
		assertEquals("test.conf", setup.getConfigFile().getName());
		assertFalse(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_withConfigFileLongOpt() throws ParseException {
		String[] args = new String[] {
				"--config", "src/test/resources/conf/test.conf",
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 8000);
		assertTrue(setup.hasConfigFile());
		assertEquals("test.conf", setup.getConfigFile().getName());
		assertFalse(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_withPortShortOpt() throws ParseException {
		String[] args = new String[] {
				"-p", "1234",
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 1234);
		assertFalse(setup.hasConfigFile());
		assertFalse(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_withPortLongOpt() throws ParseException {
		String[] args = new String[] {
				"--port", "1234",
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 1234);
		assertFalse(setup.hasConfigFile());
		assertFalse(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_withDatabaseLoggingShortOpt() throws ParseException {
		String[] args = new String[] { "-d" };
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 8000);
		assertFalse(setup.hasConfigFile());
		assertTrue(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_withDatabaseLoggingLongOpt() throws ParseException {
		String[] args = new String[] { "--enable-db-logging" };
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		
		assertEquals(setup.getPort(), 8000);
		assertFalse(setup.hasConfigFile());
		assertTrue(setup.useDatabaseLogging());
	}
	
	@Test
	public void testParseCommandLine_withHelpShortOpt() throws ParseException {
		String[] args = new String[] { "-h" };
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		assertNull(setup);
	}
	
	@Test
	public void testParseCommandLine_withHelpLongOpt() throws ParseException {
		String[] args = new String[] { "--help" };
		
		ServerSetupProvider provider = new ServerSetupProvider();
		ServerSetup setup = provider.parseCommandLine(args);
		assertNull(setup);
	}
	
	@Test
	public void testParseCommandLine_unrecognisedOption() throws ParseException {
		String[] args = new String[] {
				"-c", "src/test/resources/conf/test.conf",
				"-d",
				"-f", "43",
				"-p", "9003"
		};
		
		ServerSetupProvider provider = new ServerSetupProvider();
		UnrecognizedOptionException e = assertThrows(UnrecognizedOptionException.class, () -> {
			provider.parseCommandLine(args);
		});
		assertEquals("Unrecognized option: -f", e.getMessage());
	}
}
