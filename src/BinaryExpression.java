
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Binary expression.
 *
 * @author me
 */
public abstract class BinaryExpression extends BaseExpression implements Expression {

    private Expression left;
    private Expression right;

    /**
     * Returns first argument.
     *
     * @return first argument
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Returns second argument.
     *
     * @return second argument
     */
    public Expression getRight() {
        return right;
    }

    /**
     * Constructor.
     *
     * @param left expression
     * @param right expression
     */
    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Instantinate binary expression.
     *
     * @param l expression
     * @param r expression
     * @return BinaryExpression
     */
    protected abstract BinaryExpression inst(Expression l, Expression r);

    /**
     * Run an expression on two values.
     *
     * @param l first value
     * @param r second value
     * @return result
     */
    protected abstract double run(double l, double r);

    @Override
    public Expression assign(String var, Expression expression) {
        return inst(this.left.assign(var, expression), this.right.assign(var, expression));
    }

    @Override
    public List<String> getVariables() {
        HashSet<String> s = new HashSet<>();
        s.addAll(this.left.getVariables());
        s.addAll(this.right.getVariables());
        List<String> l = new ArrayList<>(s.size());
        l.addAll(s);
        return l;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return this.run(this.left.evaluate(assignment), this.right.evaluate(assignment));
    }

    @Override
    public double evaluate() throws Exception {
        return this.run(this.left.evaluate(), this.right.evaluate());
    }

    /**
     * Returns true if expression order matters, false if not (addition and
     * multiplication).
     *
     * @return true if order matters
     */
    protected boolean orderMatters() {
        return true;
    }

    @Override
    public boolean compare(Expression other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof BinaryExpression)) {
            return false;
        }
        BinaryExpression b = (BinaryExpression) other;
        if (this.left.compare(b.left) && this.right.compare(b.right)) {
            return true;
        }
        if (!orderMatters()) {
            if (this.left.compare(b.right) && this.right.compare(b.left)) {
                return true;
            }
        }
        return false;
    }
}
