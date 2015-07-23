package com.medallia.codefixer;

import org.junit.runner.JUnitCore;
import prototype.GsonObjectTreeNavigatorTest;

/**
 * Created by carlos on 7/21/15.
 */
public class CodeFixer {

	public static void main(String[] args) {
		JUnitCore core = new JUnitCore();

		core.run(GsonObjectTreeNavigatorTest.class);

	}
}
