
import java.util.List;
import java.util.Map;

/**
 * Unary expression.
 *
 * @author me
 */
public abstract class UnaryExpression extends BaseExpression implements Expression {

    private Expression arg;

    /**
     * Get argument.
     *
     * @return argument
     */
    public Expression getArg() {
        return arg;
    }

    /**
     * Constructor.
     *
     * @param arg argument
     */
    public UnaryExpression(Expression arg) {
        this.arg = arg;
    }

    /**
     * Instantinate unary expression.
     *
     * @param argument expression
     * @return UnaryExpression
     */
    protected abstract UnaryExpression inst(Expression argument);

    /**
     * Run an expression on one value.
     *
     * @param argument value
     * @return result
     */
    protected abstract double run(double argument);

    @Override
    public Expression assign(String var, Expression expression) {
        return inst(this.arg.assign(var, expression));
    }

    @Override
    public List<String> getVariables() {
        return this.arg.getVariables();
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return this.run(this.arg.evaluate(assignment));
    }

    @Override
    public double evaluate() throws Exception {
        return this.run(this.arg.evaluate());
    }

    @Override
    public boolean compare(Expression other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof UnaryExpression)) {
            return false;
        }
        UnaryExpression b = (UnaryExpression) other;
        if (this.arg.compare(b.arg)) {
            return true;
        }
        return false;
    }
}
