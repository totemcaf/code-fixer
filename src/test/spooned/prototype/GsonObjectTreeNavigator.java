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
		return (_s9.is("EQ") && (current == null)) || (_s9.is("NE") && (current != null)) || (_s9.is("LT") && (current < null)) || (_s9.is("GT") && (current > null)) || (_s9.is("LE") && (current <= null)) || (_s9.is("GE") && (current >= null)) ? defaultValue : current.isJsonNull() ? null : current.isJsonPrimitive() ? current.getAsString() : current.toString();
	}

	@Override
	public ObjectTreeNavigator getNavigator(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s11.is("EQ") && (current == null)) || (_s11.is("NE") && (current != null)) || (_s11.is("LT") && (current < null)) || (_s11.is("GT") && (current > null)) || (_s11.is("LE") && (current <= null)) || (_s11.is("GE") && (current >= null)) ? null : new GsonObjectTreeNavigator(current);
	}

	@Override
	public Iterable<prototype.ObjectTreeNavigator> getIterable(String fieldPath) {
		final JsonElement current = getJsonElement(fieldPath);
		return new Iterable<prototype.ObjectTreeNavigator>() {
			@Override
			public Iterator<prototype.ObjectTreeNavigator> iterator() {
				return (_s8.is("EQ") && (current == null)) || (_s8.is("NE") && (current != null)) || (_s8.is("LT") && (current < null)) || (_s8.is("GT") && (current > null)) || (_s8.is("LE") && (current <= null)) || (_s8.is("GE") && (current >= null)) ? Collections.<ObjectTreeNavigator>emptyIterator() : getIteratorForArray(((JsonArray)(current)));
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
		if ((_s10.is("EQ") && (current == null)) || (_s10.is("NE") && (current != null)) || (_s10.is("LT") && (current < null)) || (_s10.is("GT") && (current > null)) || (_s10.is("LE") && (current <= null)) || (_s10.is("GE") && (current >= null)))
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
		return (_s1.is("EQ") && (current == null)) || (_s1.is("NE") && (current != null)) || (_s1.is("LT") && (current < null)) || (_s1.is("GT") && (current > null)) || (_s1.is("LE") && (current <= null)) || (_s1.is("GE") && (current >= null)) ? false : current.getAsBoolean();
	}

	@Override
	public int getInt(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s7.is("EQ") && (current == null)) || (_s7.is("NE") && (current != null)) || (_s7.is("LT") && (current < null)) || (_s7.is("GT") && (current > null)) || (_s7.is("LE") && (current <= null)) || (_s7.is("GE") && (current >= null)) ? 0 : current.getAsInt();
	}

	@Override
	public double getDouble(String fieldPath) {
		JsonElement current = getJsonElement(fieldPath);
		return (_s6.is("EQ") && (current == null)) || (_s6.is("NE") && (current != null)) || (_s6.is("LT") && (current < null)) || (_s6.is("GT") && (current > null)) || (_s6.is("LE") && (current <= null)) || (_s6.is("GE") && (current >= null)) ? 0 : current.getAsDouble();
	}

	private JsonElement getJsonElement(String fieldPath) {
		String[] pathElements = PATH_SPLITTER_PATTERN.split(fieldPath);
		JsonElement current = tree;
		int last = (pathElements.length) - 1;
		for (int pathIndex = 0 ; (_s4.is("OR") && ((_s2.is("EQ") && ((current == null))) || (_s2.is("NE") && ((current != null))) || (_s2.is("LT") && ((current < null))) || (_s2.is("GT") && ((current > null))) || (_s2.is("LE") && ((current <= null))) || (_s2.is("GE") && ((current >= null))) || (_s3.is("EQ") && ((pathIndex == last))) || (_s3.is("NE") && ((pathIndex != last))) || (_s3.is("LT") && ((pathIndex < last))) || (_s3.is("GT") && ((pathIndex > last))) || (_s3.is("LE") && ((pathIndex <= last))) || (_s3.is("GE") && ((pathIndex >= last))))) || (_s4.is("AND") && ((_s2.is("EQ") && ((current == null))) || (_s2.is("NE") && ((current != null))) || (_s2.is("LT") && ((current < null))) || (_s2.is("GT") && ((current > null))) || (_s2.is("LE") && ((current <= null))) || (_s2.is("GE") && ((current >= null))) && (_s3.is("EQ") && ((pathIndex == last))) || (_s3.is("NE") && ((pathIndex != last))) || (_s3.is("LT") && ((pathIndex < last))) || (_s3.is("GT") && ((pathIndex > last))) || (_s3.is("LE") && ((pathIndex <= last))) || (_s3.is("GE") && ((pathIndex >= last))))) ; pathIndex++) {
			String pathElement = pathElements[pathIndex];
			if (pathElement.isEmpty()) {
			} else if (current.isJsonArray()) {
				int index = Integer.parseInt(pathElement);
				JsonArray jsonArray = ((JsonArray)(current));
				current = (_s5.is("EQ") && (index == (jsonArray.size()))) || (_s5.is("NE") && (index != (jsonArray.size()))) || (_s5.is("LT") && (index < (jsonArray.size()))) || (_s5.is("GT") && (index > (jsonArray.size()))) || (_s5.is("LE") && (index <= (jsonArray.size()))) || (_s5.is("GE") && (index >= (jsonArray.size()))) ? jsonArray.get(index) : null;
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

	private static final com.medallia.codefixer.Selector _s1 = com.medallia.codefixer.Selector.of(1,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s2 = com.medallia.codefixer.Selector.of(2,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s3 = com.medallia.codefixer.Selector.of(3,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s4 = com.medallia.codefixer.Selector.of(4,"OR","AND");

	private static final com.medallia.codefixer.Selector _s5 = com.medallia.codefixer.Selector.of(5,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s6 = com.medallia.codefixer.Selector.of(6,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s7 = com.medallia.codefixer.Selector.of(7,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s8 = com.medallia.codefixer.Selector.of(8,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s9 = com.medallia.codefixer.Selector.of(9,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s10 = com.medallia.codefixer.Selector.of(10,"EQ","NE","LT","GT","LE","GE");

	private static final com.medallia.codefixer.Selector _s11 = com.medallia.codefixer.Selector.of(11,"EQ","NE","LT","GT","LE","GE");
}

