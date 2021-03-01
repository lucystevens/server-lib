package uk.co.lukestevens.gson;

import uk.co.lukestevens.gson.GsonIgnore;

public class MockSerialisedClass {
	
	String serialisedValue = "some-value";
	
	@GsonIgnore
	String ignoredValue = "ignored";
	
	IgnoredClass ignoredClass = new IgnoredClass();
	
	@GsonIgnore
	static class IgnoredClass {
		String anotherIgnoredValue = "ignore me!";
	}

}
