package com.medallia.codefixer;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

/**
 * A selector selects one of the variants for a given hot spot
 */
public class Selector {
	private static final Map<Integer, Selector> selectors = Maps.newHashMap();

	private final int hotSpot;
	private final String[] variants;

	private int chosenVariant = -1;

	public Selector(int hotSpot, String[] variants) {
		this.hotSpot = hotSpot;
		this.variants = variants;
	}

	public static Selector of(int hotSpot, String ... variants) {
		Selector selector = new Selector(hotSpot, variants);

		selectors.put(hotSpot, selector);

		selector.choose();

		return selector;
	}

	public void choose() {
		chosenVariant = 0;
	}

	public boolean is(String variant) {
		return chosenVariant >= 0 && variants[chosenVariant].equals(variant);
	}

	@Override public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("chosenVariant", chosenVariant)
			.add("hotSpot", hotSpot)
			.add("variants", variants)
			.toString();
	}
}
