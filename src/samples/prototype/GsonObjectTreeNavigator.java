package prototype;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * {@link ObjectTreeNavigator} for a Gson representation of a Json tree ({@link JsonElement}
 */
public class GsonObjectTreeNavigator implements ObjectTreeNavigator {
	private static final Pattern PATH_SPLITTER_PATTERN = Pattern.compile("\\.*\\[|\\]\\.*|\\.+");
	private JsonElement tree;

	/**
	 * Creates an {@link ObjectTreeNavigator} taking the given {@link JsonElement} as the root of the object tree
	 * @param tree
	 */
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

		// I couldn't find it, return default value
		return current == null ? defaultValue : (current.isJsonNull() ? null : (current.isJsonPrimitive() ? current.getAsString() : current.toString()));
	}

	@Override
	public ObjectTreeNavigator getNavigator(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);

		return current == null ? null : new GsonObjectTreeNavigator(current);
	}

	@Override
	public Iterable<ObjectTreeNavigator> getIterable(String fieldPath) {
		final JsonElement current = getJsonElement(fieldPath);

		return new Iterable<ObjectTreeNavigator>() {
			@Override public Iterator<ObjectTreeNavigator> iterator() {
				return current == null
						? Collections.<ObjectTreeNavigator>emptyIterator()
						: getIteratorForArray((JsonArray) current);
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

	/**
	 * @param jsonArray the JSON array to decorate
	 * @return an {@link Iterable} that can iterate over the elements of the JSON array. The elements of the iterator
	 */
	private Iterator<ObjectTreeNavigator> getIteratorForArray(JsonArray jsonArray) {
		return Iterators.transform((jsonArray.iterator()), new Function<JsonElement, ObjectTreeNavigator>() {
			@Override public ObjectTreeNavigator apply(JsonElement element) {
				return new GsonObjectTreeNavigator(element);
			}
		});
	}

	@Override
	public Map<String, ObjectTreeNavigator> getFieldsForObject(String fieldPath) {
		final JsonElement current = getJsonElement(fieldPath);
		if (current == null)
			return Collections.emptyMap();

		Preconditions.checkArgument(current.isJsonObject(), fieldPath + " is not an object");

		Map<String, ObjectTreeNavigator> fields = Maps.newHashMap();

		for (Map.Entry<String, JsonElement> entry : ((JsonObject) current).entrySet()) {
			fields.put(entry.getKey(), new GsonObjectTreeNavigator(entry.getValue()));
		}

		return  fields;
	}

	@Override
	public boolean getBoolean(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return current == null ? false : current.getAsBoolean();
	}

	@Override
	public int getInt(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return current == null ? 0 : current.getAsInt();
	}

	@Override
	public double getDouble(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return current == null ? 0 : current.getAsDouble();
	}

	/**
	 * Navigates through the object tree following the given path.
	 * @see ObjectTreeNavigator
	 * @param fieldPath the path to follow to get the element.
	 * @return the node found at the end of the path or null if a null node is reached in any level.
	 * @throws IllegalArgumentException if a primitive value is found in the middle of the path (cannot go deeper)
	 */
	private JsonElement getJsonElement(String fieldPath) {
		String[] pathElements = PATH_SPLITTER_PATTERN.split(fieldPath);

		JsonElement current = tree;
		int last = pathElements.length - 1;

		for (int pathIndex = 0; current != null && pathIndex <= last; pathIndex++) {
			String pathElement = pathElements[pathIndex];

			if (pathElement.isEmpty()) {
				// Skip missing path part
			} else if (current.isJsonArray()) {
				int index = Integer.parseInt(pathElement);
				JsonArray jsonArray = (JsonArray) current;
				current = index < jsonArray.size() ? jsonArray.get(index) : null;
			} else if (current.isJsonObject()) {
				current = ((JsonObject) current).get(pathElement);
			} else if (current.isJsonNull()) {
				current = null;
			} else if (current.isJsonPrimitive()) {
				throw new IllegalArgumentException(String.format("%s is not a primitive value at position %s (%s)", fieldPath, pathIndex, pathElement));
			}
		}
		return current;
	}

	@Override public String toString() {
		return "GsonObjectTreeNavigator{" + tree + '}';
	}
}
