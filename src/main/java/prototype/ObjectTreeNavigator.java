package prototype;

import java.util.Map;

/**
 * Implementers allows to navigate an object tree using field paths.
 * <br/>
 * The path is a sequence of path elements separated by dots.
 * If a path element refers to an array, the path element is the array index (0 to max). In this case
 * the index could be surrounded by "[" and "]" without the dots.
 * <br/>
 * Empty path segments are ignored. Missing attributes are considered null.
 */
public interface ObjectTreeNavigator {
	/**
	 * Returns the value of a leaf in the object tree.
	 *
	 * @param fieldPath the path to traverse
	 * @param defaultValue a value to return if the element is missing. If the element is present with the null value, null is returned instead.
	 * @return the node value as a string
	 */
	String getString(String fieldPath, String defaultValue);

	/**
	 * Returns the value of a leaf in the object tree.
	 *
	 * @param fieldPath the path to traverse
	 * @return the value as a string of the property in the end of the given path or null if the path is missing.
	 */
	String getString(String fieldPath);

	/**
	 * Returns a {@link ObjectTreeNavigator} for the subtree that starts in element found in the given path.
	 *
	 * @param fieldPath the path to traverse
	 * @return an ObjectTreeNavigator for the indicated element
	 */
	ObjectTreeNavigator getNavigator(String fieldPath);

	/**
	 * Returns the value of a leave in the object tree.
	 *
	 * @param fieldPath the path to traverse
	 * @return the node value as a boolean. False if it is missing
	 */
	boolean getBoolean(String fieldPath);

	/**
	 * Returns the value of a leave in the object tree as a int.
	 *
	 * @param fieldPath the path to traverse
	 * @return the node value as an int. 0 if it is missing
	 */
	int getInt(String fieldPath);

	/**
	 * Returns the value of a leave in the object tree as a double.
	 *
	 * @param fieldPath the path to traverse
	 * @return the node value as an double. 0 if it is missing
	 */
	double getDouble(String fieldPath);

	/**
	 * Returns an {@link Iterable} for the elements that lies in the given path.
	 * <br/>
	 * Each element of the array is returned as an {@link ObjectTreeNavigator} for that element
	 *
	 * @param fieldPath the path to traverse
	 * @return an Iterable for the elements of the array
	 */
	Iterable<ObjectTreeNavigator> getIterable(String fieldPath);

	/** @return {@code true} if this element stands for a primitive value */
	boolean isPrimitive();

	/** @return {@code true} if this element stands for an object */
	boolean isObject();

	/** @return {@code true} if this element stands for an array */
	boolean isArray();

	/**
	 * @param fieldPath the path to traverse
	 * @return a {@link Map} with navigator for the fields of this object
	 */
	Map<String, ObjectTreeNavigator> getFieldsForObject(String fieldPath);
}
