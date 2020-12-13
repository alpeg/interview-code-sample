
import java.util.List;
import java.util.Map;

/**
 * Expression interface.
 *
 * @author me
 */
public interface Expression {

    /**
     * Evaluate the expression using the variable values provided in the
     * assignment, and return the result. If the expression contains a variable
     * which is not in the assignment, an exception is thrown.
     *
     * @param assignment values
     * @return evaluation result
     * @throws Exception Exception
     */
    double evaluate(Map<String, Double> assignment) throws Exception;

    /**
     * A convenience method. Like the `evaluate(assignment)` method above, but
     * uses an empty assignment.
     *
     * @return evaluation result
     * @throws Exception Exception
     */
    double evaluate() throws Exception;

    /**
     * Returns a list of the variables in the expression.
     *
     * @return variables used
     */
    List<String> getVariables();

    /**
     * Returns a nice string representation of the expression.
     *
     * @return string representation
     */
    @Override
    String toString();

    /**
     * Returns a new expression in which all occurrences of the variable var are
     * replaced with the provided expression (Does not modify the current
     * expression).
     *
     * @param var variable
     * @param expression expression to replace with
     * @return expression with variable replaced
     */
    Expression assign(String var, Expression expression);

    /**
     * Returns the expression tree resulting from differentiating the current
     * expression relative to variable `var`.
     *
     * @param var variable
     * @return derivative
     */
    Expression differentiate(String var);

    /**
     * Returned a simplified version of the current expression.
     *
     * @return simplified expression
     */
    Expression simplify();

    /**
     * Returns true if it's the same expression.
     *
     * @param other other expression
     * @return true if it's same expression
     */
    boolean compare(Expression other);
}
