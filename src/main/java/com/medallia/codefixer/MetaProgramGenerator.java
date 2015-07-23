package com.medallia.codefixer;

import java.util.EnumSet;
import java.util.stream.Collectors;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;

/**
 * Convert a program into a Meta-program
 */
public class MetaProgramGenerator extends AbstractProcessor<CtBinaryOperator<Boolean>> {

	public static final String SELECTOR_CLASS = Selector.class.getName();

	private static int index = 0;

	private static final EnumSet<BinaryOperatorKind> LOGICAL_OPERATORS = EnumSet.of(BinaryOperatorKind.AND, BinaryOperatorKind.OR);
	private static final EnumSet<BinaryOperatorKind> COMPARISON_OPERATORS = EnumSet.of(BinaryOperatorKind.EQ, BinaryOperatorKind.GE, BinaryOperatorKind.GT, BinaryOperatorKind.LE, BinaryOperatorKind.LT, BinaryOperatorKind.NE);
	private static final EnumSet<BinaryOperatorKind> REDUCED_COMPARISON_OPERATORS = EnumSet.of(BinaryOperatorKind.EQ, BinaryOperatorKind.NE);

	@Override
	public boolean isToBeProcessed(CtBinaryOperator<Boolean> element) {
		return LOGICAL_OPERATORS.contains(element.getKind()) || COMPARISON_OPERATORS.contains(element.getKind());
	}

	public void process(CtBinaryOperator<Boolean> binaryOperator) {
		BinaryOperatorKind kind = binaryOperator.getKind();

		if (LOGICAL_OPERATORS.contains(kind)) {
			// mutateOperator(binaryOperator, LOGICAL_OPERATORS);
		} else if (COMPARISON_OPERATORS.contains(kind)) {
			if (isPrimitiveNorBoolean(binaryOperator.getLeftHandOperand())
				|| isPrimitiveNorBoolean(binaryOperator.getRightHandOperand())) {
				//mutateOperator(binaryOperator, COMPARISON_OPERATORS);
			} else {
				mutateOperator(binaryOperator, REDUCED_COMPARISON_OPERATORS);
			}
		}
	}

	private boolean isPrimitiveNorBoolean(CtExpression<?> operand) {
		return operand.getType().isPrimitive() && !operand.getType().getSimpleName().equals("boolean");
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
	 * @param operators
	 */
	private void mutateOperator(final CtBinaryOperator<Boolean> operator, EnumSet<BinaryOperatorKind> operators) {
		int thisIndex = ++index;

		String newExpression = operators
			.stream()
			.map(kind -> {
				operator.setKind(kind);
				return String.format("(_s%s.is(\"%s\") && (%s))", thisIndex, kind, operator);
			})
			.collect(Collectors.joining(" || "));

		CtCodeSnippetExpression<Boolean> codeSnippet =  getFactory().Core().createCodeSnippetExpression();
		codeSnippet.setValue(newExpression);

		operator.replace(codeSnippet);

		addVariableToClass(operator, thisIndex, operators);
	}

	private void addVariableToClass(CtElement element, int index, EnumSet<BinaryOperatorKind> operators) {

		CtCodeSnippetExpression<Object> codeSnippet =  getFactory().Core().createCodeSnippetExpression();

		StringBuilder sb = new StringBuilder(SELECTOR_CLASS + ".of(").append(index);

		for (BinaryOperatorKind kind : operators) {
			sb.append(',').append('"').append(kind).append('"');
		}

		sb.append(")");

		codeSnippet.setValue(sb.toString());

		CtClass<?> type = getType(element);

		CtTypeReference<Object> fieldType = getFactory().Type().createTypeParameterReference(SELECTOR_CLASS);
		CtField<Object> field = getFactory().Field().create(type, EnumSet.of(ModifierKind.FINAL, ModifierKind.PRIVATE, ModifierKind.STATIC), fieldType, "_s" + index, codeSnippet);

		type.addField(field);
	}

	private CtClass<?> getType(CtElement element) {
		CtClass parent = element.getParent(CtClass.class);
		while (!parent.isTopLevel()) {
			parent = parent.getParent(CtClass.class);
		}

		return parent;
	}
}
