package uk.co.lukestevens.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class GsonIgnoreExclusionStrategyTest {
	
	Gson gson;
	
	@BeforeEach
	public void setup() {
		this.gson = new GsonBuilder()
				.setExclusionStrategies(new GsonIgnoreExclusionStrategy())
				.create();
	}
	
	@Test
	public void testToJson() {
		String json = gson.toJson(new MockSerialisedClass());
		assertEquals("{\"serialisedValue\":\"some-value\"}", json);
	}
	
	@Test
	public void testFromJson() {
		JsonObject json = new JsonObject();
		json.addProperty("serialisedValue", "new-value");
		json.addProperty("ignoredValue", "override-value");
		JsonObject ignoredClass = new JsonObject();
		ignoredClass.addProperty("anotherIgnoredValue", "another-value");
		json.add("ignoredClass", ignoredClass);
		
		MockSerialisedClass mock = gson.fromJson(json, MockSerialisedClass.class);
		assertEquals("new-value", mock.serialisedValue);
		assertEquals("ignored", mock.ignoredValue);
		assertEquals("ignore me!", mock.ignoredClass.anotherIgnoredValue);
	}

}
