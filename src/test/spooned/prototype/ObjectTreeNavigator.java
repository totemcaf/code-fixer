package prototype;

import java.util.Collections;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import com.google.common.collect.Maps;
import java.util.regex.Pattern;
import com.google.common.base.Preconditions;

public interface ObjectTreeNavigator {
	String getString(String fieldPath, String defaultValue);

	String getString(String fieldPath);

	ObjectTreeNavigator getNavigator(String fieldPath);

	boolean getBoolean(String fieldPath);

	int getInt(String fieldPath);

	double getDouble(String fieldPath);

	Iterable<prototype.ObjectTreeNavigator> getIterable(String fieldPath);

	boolean isPrimitive();

	boolean isObject();

	boolean isArray();

	Map<java.lang.String, prototype.ObjectTreeNavigator> getFieldsForObject(String fieldPath);
}

