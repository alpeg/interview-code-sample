
import java.util.Objects;

/**
 *
 * @author me
 */
public abstract class BaseExpression implements Expression {

    /**
     * Utility function to check if two doubles are approximately equal
     * (difference is less than 0.00000001).
     *
     * @param a first number
     * @param b second number
     * @return true if difference is less than 0.00000001
     */
    public static boolean equalsAppr(Double a, Double b) {
        if (a == null || b == null) {
            return Objects.equals(a, b);
        }
        return Math.abs(a - b) < 0.00000001;
    }

//    /**
//     * Check if expression is a constant and equals to value provided.
//     *
//     * @param e Expression
//     * @param v Value
//     * @return true if expression evaluates to value
//     */
//    public static boolean constAndEquals(Expression e, Double v) {
//        try {
//            return e.getVariables().isEmpty() && equalsAppr(e.evaluate(), v);
//        } catch (Exception ex) {
//        }
//        return false;
//    }
    /**
     * Check if expression is a constant and returns it's calculated value if
     * possible.
     *
     * @param e Expression
     * @return null|Double
     */
    public static Double constValueOrNull(Expression e) {
        try {
            if (e.getVariables().isEmpty()) {
                return e.evaluate();
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

//    @Override
//    public Expression simplify() {
//        if (this.getVariables().isEmpty()) {
//            try {
//                double result = this.evaluate();
//                return new Num(result);
//            } catch (Exception e) {
//                return this;
//            }
//        }
//    }
    @Override
    public abstract boolean compare(Expression e);
}
