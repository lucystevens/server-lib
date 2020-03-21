package uk.co.lukestevens.server;

import uk.co.lukestevens.gson.GsonIgnore;

public class MockSerialisedClass {
	
	String serialisedValue = "some-value";
	
	@GsonIgnore
	String ignoredValue = "ignored";

}
