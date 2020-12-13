
import java.util.List;
import java.util.Map;

/**
 * Constant value.
 *
 * @author me
 */
public class Const extends BaseExpression implements Expression {

    public static final Const PI = new Const("pi", Math.PI);
    public static final Const E = new Const("e", Math.E);

    private String name;
    private double value;

    /**
     * Constructor.
     *
     * @param name constant name
     * @param value constant value
     */
    public Const(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return value;
    }

    @Override
    public double evaluate() throws Exception {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public List<String> getVariables() {
        return List.of();
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return this;
    }

    @Override
    public Expression differentiate(String var) {
        return new Num(0);
    }

    @Override
    public Expression simplify() {
        return new Num(this.value);
    }

    @Override
    public boolean compare(Expression e) {
        try {
            return e.getVariables().isEmpty() && BaseExpression.equalsAppr(this.value, e.evaluate());
        } catch (Exception ex) {
            return false;
        }
    }

}
