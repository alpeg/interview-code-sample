
/**
 * Arbitrary base logariphm.
 *
 * @author me
 */
public class Log extends BinaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param base base
     * @param exponent exponent
     */
    public Log(Expression base, Expression exponent) {
        super(base, exponent);
    }

    @Override
    protected BinaryExpression inst(Expression left, Expression right) {
        return new Log(left, right);
    }

    @Override
    protected double run(double left, double right) {
        return Math.log(right) / Math.log(left); // log_a b = ln(b)/ln(a)
    }

    @Override
    public String toString() {
        return "log(" + this.getLeft() + ", " + this.getRight() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        boolean leftR = this.getLeft().getVariables().indexOf(var) == -1;
        boolean rightR = this.getRight().getVariables().indexOf(var) == -1;
        if (leftR && rightR) { // log_c c = const, c' = 0
            return new Num(0);
        }
        if (leftR && !rightR) { // (log_a u)' = u'/(u ln a)
            return new Div(this.getRight().differentiate(var),
                    new Mult(this.getRight(), new Log(Const.E, this.getLeft()))
            );
        }
        // log_x a = ln(a)/ln(x);
        return new Div(new Log(Const.E, this.getRight()), new Log(Const.E, this.getLeft())).differentiate(var);
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
        // log(x,x)=1
        if (leftSimple.compare(rightSimple)) {
            return new Num(1d);
        }

        Expression r = this.inst(leftSimple, rightSimple);
        return r;
    }

}
