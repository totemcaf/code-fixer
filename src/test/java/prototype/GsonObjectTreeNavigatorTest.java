package prototype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Iterator;

import com.google.gson.JsonParser;
import org.junit.Test;

/**
 * Test the {@link GsonObjectTreeNavigator} class
 */
public class GsonObjectTreeNavigatorTest {


	private static final JsonParser PARSER = new JsonParser();

	/** Verifies getting a property of an object */
	@Test
	public void testSimpleObject() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'a':10}");

		assertEquals("10", navigator.getString("a"));
	}

	/** Test case sensitive attributes */
	@Test
	public void testCaseSensitiveAttributeName() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'MalColm':168}");

		assertEquals("168", navigator.getString("MalColm"));
		assertNull(navigator.getString("malcolm"));
		assertNull(navigator.getString("MALCOLM"));
	}

	/** Verifies getting an element of an array of primitive values */
	@Test
	public void testArrayOfPrimitives() {
		ObjectTreeNavigator navigator = makeNavigatorFor("[10,20,30,'aa', 40]");

		assertEquals("10", navigator.getString("[0]"));
		assertEquals("aa", navigator.getString("[3]"));
		assertEquals("40", navigator.getString("[4]"));
	}

	/** Verifies getting a property of an object that is an element of an array */
	@Test
	public void testArrayOfObjects() {
		ObjectTreeNavigator navigator = makeNavigatorFor("[{'a':10,'name':'Charly'},{'a':20,'name':'Fernando'},{'a':30,'name':'Richard'},{'a':40,'name':'Esteban'}]");

		assertEquals("10", navigator.getString("[0].a"));
		assertEquals("Charly", navigator.getString("[0].name"));
		assertEquals("20", navigator.getString("[1].a"));
		assertEquals("Fernando", navigator.getString("[1].name"));
		assertEquals("30", navigator.getString("[2].a"));
		assertEquals("Richard", navigator.getString("[2].name"));
		assertEquals("40", navigator.getString("[3].a"));
		assertEquals("Esteban", navigator.getString("[3].name"));
	}

	/** Verifies getting an element of array that is a property of an object */
	@Test
	public void testObjectWithArrayOfPrimitives() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'title':'Something','errors':['An error', 'Other error']}");
		assertEquals("Other error", navigator.getString("errors[1]"));
	}

	/** Verifies getting a property of an object that is an element of array that is a property of an object */
	@Test
	public void testObjectWithArrayOfObjects() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'title':'Something','errors':[{'code':10,'description':'An error'}, {'code':30,'description':'Other error'}]}");
		assertEquals("Other error", navigator.getString("errors[1].description"));
		assertEquals("10", navigator.getString("errors[0].code"));
	}

	/** Verifies getting a property that is not present in the JSON */
	@Test
	public void testMissingPropertyInObject() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'title':'Something','errors':['An error', 'Other error']}");
		assertNull(navigator.getString("name"));
	}

	/** Verifies getting a property that is not present in the JSON */
	@Test
	public void testEmptyPathSegments() {
		ObjectTreeNavigator navigator = makeNavigatorFor("[{'a':10,'name':'Charly'},{'a':20,'name':'Fernando'},{'a':30,'name':'Richard'},{'a':40,'name':'Esteban'}]");
		assertEquals("Fernando", navigator.getString("......[1]....name..."));
	}

	/** Verifies getting a property that is not present in the JSON */
	@Test
	public void testMissingProperty() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'title':'Something','errors':['An error', 'Other error'], 'reviews':null}");
		assertNull(navigator.getString("reviews[4].name"));
	}

	/** Verifies getting a property that is {@code null} in the JSON. It should NOT return the string {@code "null"} */
	@Test
	public void testNullProperty() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'title':'Something','errors':['An error', 'Other error'], 'reviews':[{'name':null, 'test': 'value'}]}");
		assertEquals("value", navigator.getString("reviews[0].test"));
		assertNull(navigator.getString("reviews[0].name"));
	}

	/** Verifies getting an array element with an index greater than the last element of the array */
	@Test
	public void testIndexGreaterThanMaximum() {
		ObjectTreeNavigator navigator = makeNavigatorFor("[10,20,30,'aa', 40]");
		assertNull(navigator.getString("[5]"));
	}

	/** Verifies getting an array element with an invalid index (negative) */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testNegativeIndex() {
		ObjectTreeNavigator navigator = makeNavigatorFor("[10,20,30,'aa', 40]");
		navigator.getString("[-1]");
	}

	/** Verifies the access to a deep property using a navigator for sub graph of the JSON */
	@Test
	public void testSubNavigator() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'title':'Something','errors':[{'code':10,'description':'An error'}, {'code':30,'description':'Other error'}]}");
		ObjectTreeNavigator errorsNavigator = navigator.getNavigator("errors");
		ObjectTreeNavigator firstErrorsNavigatorA = navigator.getNavigator("errors[1]");
		ObjectTreeNavigator firstErrorsNavigatorB = errorsNavigator.getNavigator("[1]");

		assertEquals("Other error", navigator.getString("errors[1].description"));
		assertEquals("Other error", errorsNavigator.getString("[1].description"));
		assertEquals("Other error", firstErrorsNavigatorA.getString("description"));
		assertEquals("Other error", firstErrorsNavigatorB.getString("description"));
	}

	/** Test accessing to the elements of an array through an iterable */
	@Test
	public void testGettingAIterable() {
		ObjectTreeNavigator navigator = makeNavigatorFor("{'total':40, 'items':[{'a':10,'name':'Charly'},{'a':20,'name':'Fernando'},{'a':30,'name':'Richard'},{'a':40,'name':'Esteban'}]}");

		Iterable<ObjectTreeNavigator> items = navigator.getIterable("items");

		Iterator<ObjectTreeNavigator> iterator = items.iterator();

		assertEquals("Charly", iterator.next().getString("name"));
		assertEquals("Fernando", iterator.next().getString("name"));
		assertEquals("Richard", iterator.next().getString("name"));
		assertEquals("Esteban", iterator.next().getString("name"));
		assertFalse(iterator.hasNext());
	}

	/**
	 * @param jsonText the JSON as a string. Single quotes will be replaced with double quotes to make the JSON literal more readable in the code.
	 * @return a {@link ObjectTreeNavigator} for the give JSON as a string.
	 */
	private ObjectTreeNavigator makeNavigatorFor(String jsonText) {
		return new GsonObjectTreeNavigator(PARSER.parse(jsonText.replace('\'', '"')));
	}
}
