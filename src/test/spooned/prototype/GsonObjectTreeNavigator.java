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
		return (_op(8, "EQ") && (current == null)) || (_op(8, "NE") && (current != null)) || (_op(8, "LT") && (current < null)) || (_op(8, "GT") && (current > null)) || (_op(8, "LE") && (current <= null)) || (_op(8, "GE") && (current >= null)) ? defaultValue : current.isJsonNull() ? null : current.isJsonPrimitive() ? current.getAsString() : current.toString();
	}

	@Override
	public ObjectTreeNavigator getNavigator(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_op(10, "EQ") && (current == null)) || (_op(10, "NE") && (current != null)) || (_op(10, "LT") && (current < null)) || (_op(10, "GT") && (current > null)) || (_op(10, "LE") && (current <= null)) || (_op(10, "GE") && (current >= null)) ? null : new GsonObjectTreeNavigator(current);
	}

	@Override
	public Iterable<prototype.ObjectTreeNavigator> getIterable(String fieldPath) {
		final JsonElement current = getJsonElement(fieldPath);
		return new Iterable<prototype.ObjectTreeNavigator>() {
			@Override
			public Iterator<prototype.ObjectTreeNavigator> iterator() {
				return (_op(7, "EQ") && (current == null)) || (_op(7, "NE") && (current != null)) || (_op(7, "LT") && (current < null)) || (_op(7, "GT") && (current > null)) || (_op(7, "LE") && (current <= null)) || (_op(7, "GE") && (current >= null)) ? Collections.<ObjectTreeNavigator>emptyIterator() : getIteratorForArray(((JsonArray)(current)));
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
		if ((_op(9, "EQ") && (current == null)) || (_op(9, "NE") && (current != null)) || (_op(9, "LT") && (current < null)) || (_op(9, "GT") && (current > null)) || (_op(9, "LE") && (current <= null)) || (_op(9, "GE") && (current >= null)))
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
		return (_op(1, "EQ") && (current == null)) || (_op(1, "NE") && (current != null)) || (_op(1, "LT") && (current < null)) || (_op(1, "GT") && (current > null)) || (_op(1, "LE") && (current <= null)) || (_op(1, "GE") && (current >= null)) ? false : current.getAsBoolean();
	}

	@Override
	public int getInt(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_op(6, "EQ") && (current == null)) || (_op(6, "NE") && (current != null)) || (_op(6, "LT") && (current < null)) || (_op(6, "GT") && (current > null)) || (_op(6, "LE") && (current <= null)) || (_op(6, "GE") && (current >= null)) ? 0 : current.getAsInt();
	}

	@Override
	public double getDouble(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_op(5, "EQ") && (current == null)) || (_op(5, "NE") && (current != null)) || (_op(5, "LT") && (current < null)) || (_op(5, "GT") && (current > null)) || (_op(5, "LE") && (current <= null)) || (_op(5, "GE") && (current >= null)) ? 0 : current.getAsDouble();
	}

	private JsonElement getJsonElement(String fieldPath) {
		String[] pathElements = PATH_SPLITTER_PATTERN.split(fieldPath);
		JsonElement current = tree;
		int last = (pathElements.length) - 1;
		for (int pathIndex = 0 ; (_op(2, "EQ") && ((current == null))) || (_op(2, "NE") && ((current != null))) || (_op(2, "LT") && ((current < null))) || (_op(2, "GT") && ((current > null))) || (_op(2, "LE") && ((current <= null))) || (_op(2, "GE") && ((current >= null))) && (_op(3, "EQ") && ((pathIndex == last))) || (_op(3, "NE") && ((pathIndex != last))) || (_op(3, "LT") && ((pathIndex < last))) || (_op(3, "GT") && ((pathIndex > last))) || (_op(3, "LE") && ((pathIndex <= last))) || (_op(3, "GE") && ((pathIndex >= last))) ; pathIndex++) {
			String pathElement = pathElements[pathIndex];
			if (pathElement.isEmpty()) {
			} else if (current.isJsonArray()) {
				int index = Integer.parseInt(pathElement);
				JsonArray jsonArray = ((JsonArray)(current));
				current = (_op(4, "EQ") && (index == (jsonArray.size()))) || (_op(4, "NE") && (index != (jsonArray.size()))) || (_op(4, "LT") && (index < (jsonArray.size()))) || (_op(4, "GT") && (index > (jsonArray.size()))) || (_op(4, "LE") && (index <= (jsonArray.size()))) || (_op(4, "GE") && (index >= (jsonArray.size()))) ? jsonArray.get(index) : null;
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
}

