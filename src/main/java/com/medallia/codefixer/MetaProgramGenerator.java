package com.medallia.codefixer;

import java.util.EnumSet;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtCodeSnippetExpression;

/**
 * Convert a program into a Meta-program
 */
public class MetaProgramGenerator extends AbstractProcessor<CtBinaryOperator<Boolean>> {

	private static int index = 0;

	private static final EnumSet<BinaryOperatorKind> LOGICAL_OPERATORS = EnumSet.of(BinaryOperatorKind.AND, BinaryOperatorKind.OR);
	private static final EnumSet<BinaryOperatorKind> COMPARISON_OPERATORS = EnumSet.of(BinaryOperatorKind.EQ, BinaryOperatorKind.GE, BinaryOperatorKind.GT, BinaryOperatorKind.LE, BinaryOperatorKind.LT, BinaryOperatorKind.NE);

	@Override
	public boolean isToBeProcessed(CtBinaryOperator<Boolean> element) {
		return LOGICAL_OPERATORS.contains(element.getKind()) || COMPARISON_OPERATORS.contains(element.getKind());
	}

	public void process(CtBinaryOperator<Boolean> binaryOperator) {
		BinaryOperatorKind kind = binaryOperator.getKind();

		if (LOGICAL_OPERATORS.contains(kind)) {
			mutateLogicalOperator(binaryOperator);
		} else if (COMPARISON_OPERATORS.contains(kind)) {
			mutateComparisonOperator(binaryOperator);
		}
	}

	/**
	 * Converts "a op b" bean op one of "<", "<=", "==", ">=", "!=" to:
	 *    (  (op(1, 0, "<")  && (a < b))
	 *    || (op(1, 1, "<=") && (a <= b))
	 *    || (op(1, 2, "==") && (a == b))
	 *    || (op(1, 3, ">=") && (a >= b))
	 *    || (op(1, 4, ">")  && (a > b))
	 *    )
	 *
	 * com.medallia.codefixer
	 * @param operator
	 */
	private void mutateComparisonOperator(final CtBinaryOperator<Boolean> operator) {
		System.out.println("> " + operator);

		int thisIndex = ++index;


		String newExpression = COMPARISON_OPERATORS
			.stream()
			.map(kind -> {
				operator.setKind(kind);
				return String.format("(_op(%s, \"%s\") && (%s))", thisIndex, kind, operator);
			})
			.collect(Collectors.joining(" || "));

		System.out.println(">>> " + newExpression);

		CtCodeSnippetExpression<Boolean> codeSnippet =  getFactory().Core().createCodeSnippetExpression();
		codeSnippet.setValue(newExpression);

		operator.replace(codeSnippet);
	}

	private void mutateLogicalOperator(CtBinaryOperator<Boolean> operator) {
	}
}
