
/**
 *
 * @author me
 */
public class Plus extends BinaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param augend augend
     * @param addend addend
     */
    public Plus(Expression augend, Expression addend) {
        super(augend, addend);
    }

    @Override
    protected BinaryExpression inst(Expression left, Expression right) {
        return new Plus(left, right);
    }

    @Override
    protected double run(double left, double right) {
        return left + right;
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + " + " + this.getRight() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        return this.inst(this.getLeft().differentiate(var), this.getRight().differentiate(var));
    }

    @Override
    protected boolean orderMatters() {
        return false;
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
        // 0+x, x+0
        if (BaseExpression.equalsAppr(leftValue, 0d)) {
            return rightSimple;
        }
        if (BaseExpression.equalsAppr(rightValue, 0d)) {
            return leftSimple;
        }
        // x+(-x) = 0, -x+x = 0
        if ((leftSimple instanceof Neg) || (rightSimple instanceof Neg)) {
            Expression a = leftSimple;
            Expression b = rightSimple;
            while ((a instanceof Neg) && (b instanceof Neg)) {
                a = ((Neg) a).getArg();
                b = ((Neg) b).getArg();
            }
            if (((a instanceof Neg) && ((Neg) a).getArg().compare(b))
                    || ((b instanceof Neg) && ((Neg) b).getArg().compare(a))) {
                return new Num(0);
            }
        }
        Expression r = this.inst(leftSimple, rightSimple);
        return r;
    }
}
