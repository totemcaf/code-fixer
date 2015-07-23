package com.medallia.codefixer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import prototype.GsonObjectTreeNavigator;
import prototype.GsonObjectTreeNavigatorTest;

/**
 * Run the tests against all the variations to choose a candidate fix
 *
 * More work should be done to handle conflicting hot spots
 */
public class CodeFixer {

	public static final Class<GsonObjectTreeNavigatorTest> TEST_CLASS = GsonObjectTreeNavigatorTest.class;

	public static void main(String[] args) {

		boolean debug = ImmutableList.copyOf(args).contains("debug");

		new GsonObjectTreeNavigator(null);

		JUnitCore core = new JUnitCore();

		if (debug)
			core.addListener(new TextListener(System.out));

		List<Selector> selectors = Selector.allSelectors();

		if (selectors.isEmpty())
			// There's no hot spot in program. Add one to run it at least once
			selectors = ImmutableList.of(Selector.of(0, "n/a"));

		List<String> successes = Lists.newArrayList();
		List<String> failures = Lists.newArrayList();
		Multimap<Integer, String> failures2 = Multimaps.newListMultimap(Maps.newHashMap(), Lists::newArrayList);

		// Execute the test for each hot spot permutation
		for (int options[] : permutations(selectors.stream().map(Selector::getOptionCount).collect(Collectors.toList()))) {

			for (int i = options.length - 1; i >= 0; i--) {
				selectors.get(i).choose(options[i]);
				selectors.get(i).setStopTime(System.currentTimeMillis() + 30_000);
			}

			if (debug)
				System.out.println("Checking options: " + Arrays.toString(options));

			Result result = core.run(TEST_CLASS);

			if (result.wasSuccessful())
				successes.add("   Worked !!!  -> " + Arrays.toString(options));
			else {
				String txt = String.format("%s -> It has %s failures out of %s runs in %s ms", Arrays.toString(options), result.getFailureCount(), result.getRunCount(), result.getRunTime());
				failures.add(txt);
				failures2.put(result.getFailureCount(), txt);
			}
		}

		// Show result summary
		Sets.newHashSet(failures2.keys()).forEach(k -> {
			System.out.println(String.format("\n-- Cases with %s", k));
			Collection<String> texts = failures2.get(k);
			if (k <= 2 || texts.size() < 10)
				texts.forEach(System.out::println);
			else
				System.out.println("There are " + texts.size());
		});

		// failures.forEach(System.out::println);

		System.out.println();

		if (successes.isEmpty())
			System.out.println("Oops, sorry, we could find a successful option");
		else
			successes.forEach(System.out::println);
	}

	/**
	 * Computes an iterable though all the permutations or the values in the ranges provided
	 * @param sizes the number of elements in each range (from 0 to size - 1)
	 * @return an Iterable
	 */
	private static Iterable<int[]> permutations(List<Integer> sizes) {
		int limits[] = new int[sizes.size()];

		int last = sizes.size() - 1;
		for (int i = last; i >= 0; i--)
			limits[i] = sizes.get(i) - 1;


		return () -> new Iterator<int[]>() {
			int current[] = new int[last + 1];

			{
				current[last]--;  // Force the first element
			}

			@Override public boolean hasNext() {
				return !Arrays.equals(limits, current);
			}

			@Override public int[] next() {
				for (int i = last; i >= 0; i--) {
					if (current[i] < limits[i]) {
						current[i] ++;

						return current.clone();
					}
					current[i] = 0;
				}

				return new int[0];
			}
		};
	}
}
