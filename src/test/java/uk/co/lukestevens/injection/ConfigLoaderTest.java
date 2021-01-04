package uk.co.lukestevens.injection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import uk.co.lukestevens.config.Config;

public class ConfigLoaderTest {
	
	@Test
	public void testInitialise_differentConfigs() throws IOException {
		Config setupConfig = mock(Config.class);
		Config appConfig = mock(Config.class);
		assertTrue(setupConfig != appConfig);
		
		ConfigLoader loader = new ConfigLoader(setupConfig, appConfig);
		loader.initialise();
		
		verify(setupConfig, times(1)).load();
		verify(appConfig, times(1)).load();
	}
	
	@Test
	public void testInitialise_sameConfig() throws IOException {
		Config config = mock(Config.class);
		
		ConfigLoader loader = new ConfigLoader(config, config);
		loader.initialise();
		
		verify(config, times(1)).load();
	}

}
