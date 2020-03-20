package uk.co.lukestevens.server;

import java.io.File;

import uk.co.lukestevens.cli.setup.KeyBasedSetup;

public class MockSetup extends KeyBasedSetup {
	
	@Override
	public File getConfigFile() {
		return new File("src/test/resources/conf/test.conf");
	}
	
	@Override
	public String getKey() {
		return "secretkey";
	}

}
