
/**
 * Subtraction.
 *
 * @author me
 */
public class Minus extends BinaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param minuend minuend
     * @param subtrahend subtrahend
     */
    public Minus(Expression minuend, Expression subtrahend) {
        super(minuend, subtrahend);
    }

    @Override
    protected BinaryExpression inst(Expression left, Expression right) {
        return new Minus(left, right);
    }

    @Override
    protected double run(double left, double right) {
        return left - right;
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + " - " + this.getRight() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        return this.inst(this.getLeft().differentiate(var), this.getRight().differentiate(var));
    }

    @Override
    public Expression simplify() {
        Expression leftSimple = this.getLeft().simplify();
        Expression rightSimple = this.getRight().simplify();
        Double leftValue = BaseExpression.constValueOrNull(leftSimple);
        Double rightValue = BaseExpression.constValueOrNull(rightSimple);

        // both are const
        if (leftValue != null && rightValue != null) {
            try {
                return new Num(this.evaluate());
            } catch (Exception ex) {
                // do not simplify if it causes an exception to occur.
                ex.toString(); // empty block will cause checkstyle to complain
            }
        }
        // 0-x = -x
        if (BaseExpression.equalsAppr(leftValue, 0d)) {
            if (rightSimple instanceof Neg) {
                return ((Neg) rightSimple).getArg();
            }
            return new Neg(rightSimple);
        }
        // x-0 = x
        if (BaseExpression.equalsAppr(rightValue, 0d)) {
            return leftSimple;
        }
        if (leftSimple.compare(rightSimple)) {
            return new Num(0d);
        }
        Expression r = this.inst(leftSimple, rightSimple);
        // x-(-y)=x+y
        if (rightSimple instanceof Neg) {
            r = new Plus(leftSimple, ((Neg) rightSimple).getArg());
        }
        // if (this.left != leftSimple || this.right != rightSimple)r = r.simplify();
        return r;
    }
}
