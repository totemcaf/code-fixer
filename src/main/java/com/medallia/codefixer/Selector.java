package com.medallia.codefixer;

import java.util.List;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * A selector selects one of the variants for a given hot spot
 */
public class Selector {
	private static final Map<Integer, Selector> selectors = Maps.newHashMap();

	private final int hotSpot;
	private final String[] variants;

	private int chosenVariant = -1; // TODO ser default base on original source
	private long stopTime;

	public Selector(int hotSpot, String[] variants) {
		this.hotSpot = hotSpot;
		this.variants = variants;
	}

	public static Selector of(int hotSpot, String ... variants) {
		Selector selector = new Selector(hotSpot, variants);

		selectors.put(hotSpot, selector);

		return selector;
	}

	public void choose(int option) {
		chosenVariant = option;
	}

	public boolean is(String variant) {
		if (System.currentTimeMillis() > stopTime)
			throw new StopTimeExceededError("In selector " + hotSpot + " with option " + chosenVariant + " checking for " + variant);

		return chosenVariant >= 0 && variants[chosenVariant].equals(variant);
	}

	@Override public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("chosenVariant", chosenVariant)
			.add("hotSpot", hotSpot)
			.add("variants", variants)
			.toString();
	}

	public static List<Selector> allSelectors() {
		return ImmutableList.copyOf(selectors.values());
	}

	public int getOptionCount() {
		return variants.length;
	}

	public void setStopTime(long stopTime) {

		this.stopTime = stopTime;
	}

	public static class StopTimeExceededError extends RuntimeException {   // TODO THis can be cached !!

		public StopTimeExceededError(String message) {
			super(message);
		}
	}
}
