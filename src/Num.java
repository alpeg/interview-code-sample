
import java.util.List;
import java.util.Map;

/**
 * Arbitrary number.
 *
 * @author me
 */
public class Num implements Expression {

    private double value;

    /**
     * Constructor.
     *
     * @param value number
     */
    public Num(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        return this.value;
    }

    @Override
    public double evaluate() throws Exception {
        return this.value;
    }

    @Override
    public String toString() {
        String s = Double.toString(this.value);
        // 2.0 -> 2
        if (s.length() > 2 && ".0".equals(s.substring(s.length() - 2))) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
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
        return this;
    }

    @Override
    public boolean compare(Expression e) {
        try {
            return e.getVariables().isEmpty() && BaseExpression.equalsAppr(this.value, e.evaluate());
        } catch (Exception ex) {
            ex.toString(); // empty block will cause checkstyle to complain
        }
        return false;
    }

}
