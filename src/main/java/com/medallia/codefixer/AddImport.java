package com.medallia.codefixer;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;

/**
 * Created by carlos on 7/22/15.
 */
public class AddImport extends AbstractProcessor<CtType> {
	@Override public void process(CtType element) {
		CtField field = getFactory().Field().create(null, null, null, null);
		element.addField(field);
	}
}
