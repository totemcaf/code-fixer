package prototype;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonObjectTreeNavigator implements ObjectTreeNavigator {
	private static final Pattern PATH_SPLITTER_PATTERN = Pattern.compile("\\.*\\[|\\]\\.*|\\.+");

	private JsonElement tree;

	public GsonObjectTreeNavigator(JsonElement tree) {
		this.tree = tree;
	}

	@Override
	public String getString(String fieldPath) {
		return getString(fieldPath, null);
	}

	@Override
	public String getString(String fieldPath, String defaultValue) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s8.is("EQ") && (current == null)) || (_s8.is("NE") && (current != null)) ? defaultValue : current.isJsonNull() ? null : current.isJsonPrimitive() ? current.getAsString() : current.toString();
	}

	@Override
	public ObjectTreeNavigator getNavigator(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s10.is("EQ") && (current == null)) || (_s10.is("NE") && (current != null)) ? null : new GsonObjectTreeNavigator(current);
	}

	@Override
	public Iterable<prototype.ObjectTreeNavigator> getIterable(String fieldPath) {
		final JsonElement current = getJsonElement(fieldPath);
		return new Iterable<prototype.ObjectTreeNavigator>() {
			@Override
			public Iterator<prototype.ObjectTreeNavigator> iterator() {
				return (_s7.is("EQ") && (current == null)) || (_s7.is("NE") && (current != null)) ? Collections.<ObjectTreeNavigator>emptyIterator() : getIteratorForArray(((JsonArray)(current)));
			}
		};
	}

	@Override
	public boolean isPrimitive() {
		return tree.isJsonPrimitive();
	}

	@Override
	public boolean isObject() {
		return tree.isJsonObject();
	}

	@Override
	public boolean isArray() {
		return tree.isJsonArray();
	}

	private Iterator<prototype.ObjectTreeNavigator> getIteratorForArray(JsonArray jsonArray) {
		return Iterators.transform(jsonArray.iterator(), new com.google.common.base.Function<com.google.gson.JsonElement, prototype.ObjectTreeNavigator>() {
			@Override
			public ObjectTreeNavigator apply(JsonElement element) {
				return new GsonObjectTreeNavigator(element);
			}
		});
	}

	@Override
	public Map<java.lang.String, prototype.ObjectTreeNavigator> getFieldsForObject(String fieldPath) {
		final JsonElement current = getJsonElement(fieldPath);
		if ((_s9.is("EQ") && (current == null)) || (_s9.is("NE") && (current != null)))
			return Collections.emptyMap();
		
		Preconditions.checkArgument(current.isJsonObject(), (fieldPath + " is not an object"));
		Map<java.lang.String, prototype.ObjectTreeNavigator> fields = Maps.newHashMap();
		for (Map.Entry<java.lang.String, com.google.gson.JsonElement> entry : ((JsonObject)(current)).entrySet()) {
			fields.put(entry.getKey(), new GsonObjectTreeNavigator(entry.getValue()));
		}
		return fields;
	}

	@Override
	public boolean getBoolean(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s1.is("EQ") && (current == null)) || (_s1.is("NE") && (current != null)) ? false : current.getAsBoolean();
	}

	@Override
	public int getInt(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s6.is("EQ") && (current == null)) || (_s6.is("NE") && (current != null)) ? 0 : current.getAsInt();
	}

	@Override
	public double getDouble(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s5.is("EQ") && (current == null)) || (_s5.is("NE") && (current != null)) ? 0 : current.getAsDouble();
	}

	private JsonElement getJsonElement(String fieldPath) {
		String[] pathElements = PATH_SPLITTER_PATTERN.split(fieldPath);
		JsonElement current = tree;
		int last = (pathElements.length) - 1;
		for (int pathIndex = 0 ; (_s2.is("EQ") && ((current == null))) || (_s2.is("NE") && ((current != null))) && (_s3.is("EQ") && ((pathIndex == last))) || (_s3.is("NE") && ((pathIndex != last))) || (_s3.is("LT") && ((pathIndex < last))) || (_s3.is("GT") && ((pathIndex > last))) || (_s3.is("LE") && ((pathIndex <= last))) || (_s3.is("GE") && ((pathIndex >= last))) ; pathIndex++) {
			String pathElement = pathElements[pathIndex];
			if (pathElement.isEmpty()) {
			} else if (current.isJsonArray()) {
				int index = Integer.parseInt(pathElement);
				JsonArray jsonArray = ((JsonArray)(current));
				current = (_s4.is("EQ") && (index == (jsonArray.size()))) || (_s4.is("NE") && (index != (jsonArray.size()))) || (_s4.is("LT") && (index < (jsonArray.size()))) || (_s4.is("GT") && (index > (jsonArray.size()))) || (_s4.is("LE") && (index <= (jsonArray.size()))) || (_s4.is("GE") && (index >= (jsonArray.size()))) ? jsonArray.get(index) : null;
			} else if (current.isJsonObject()) {
				current = ((JsonObject)(current)).get(pathElement);
			} else if (current.isJsonNull()) {
				current = null;
			} else if (current.isJsonPrimitive()) {
				throw new IllegalArgumentException(String.format("%s is not a primitive value at position %s (%s)", fieldPath, pathIndex, pathElement));
			} 
		}
		return current;
	}

	@Override
	public String toString() {
		return ("GsonObjectTreeNavigator{" + (tree)) + '}';
	}

	private static final com.medallia.codefixer.Selector _s1 = com.medallia.codefixer.Selector.of(1,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s2 = com.medallia.codefixer.Selector.of(2,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s3 = com.medallia.codefixer.Selector.of(3,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s4 = com.medallia.codefixer.Selector.of(4,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s5 = com.medallia.codefixer.Selector.of(5,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s6 = com.medallia.codefixer.Selector.of(6,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s7 = com.medallia.codefixer.Selector.of(7,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s8 = com.medallia.codefixer.Selector.of(8,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s9 = com.medallia.codefixer.Selector.of(9,"EQ","NE");

	private static final com.medallia.codefixer.Selector _s10 = com.medallia.codefixer.Selector.of(10,"EQ","NE");
}

