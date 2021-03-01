package uk.co.lukestevens.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * An exclusion strategy to prevent fields and classes annotated with {@link GsonIgnore}
 * from being serialised by Gson.
 * 
 * @author Luke Stevens
 */
public class GsonIgnoreExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(GsonIgnore.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return clazz.getAnnotation(GsonIgnore.class) != null;
	}

}
