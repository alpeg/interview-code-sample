
/**
 *
 * @author me
 */
public class Pow extends BinaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param base base
     * @param exponent exponent
     */
    public Pow(Expression base, Expression exponent) {
        super(base, exponent);
    }

    @Override
    protected BinaryExpression inst(Expression left, Expression right) {
        return new Pow(left, right);
    }

    @Override
    protected double run(double left, double right) {
        return Math.pow(left, right);
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + "^" + this.getRight() + ")";
    }

    @Override
    public Expression differentiate(String var) {

        boolean leftR = this.getLeft().getVariables().indexOf(var) == -1;
        boolean rightR = this.getRight().getVariables().indexOf(var) == -1;
        if (leftR && rightR) { // c^c, c' = 0
            return new Num(0);
        }
        if (!leftR && rightR) { // (u^m)' = m u^(m-1) u', m in R
            return new Mult(new Mult(
                    this.getRight(), // m
                    inst(this.getLeft(), new Minus(this.getRight(), new Num(1d)))), // u^(m-1)
                    this.getLeft().differentiate(var) // u'
            );
        }
        if (leftR && !rightR) { // (a^u)' = a^u * ln a * u'
            return new Mult(new Mult(
                    this, // a^u
                    new Log(Const.E, this.getLeft())), // ln a
                    this.getRight().differentiate(var) // u'
            );
        }
        // !leftR && !rightR
        // https://en.wikipedia.org/wiki/Differentiation_rules#Generalized_power_rule
        // (f^g)' = f^g*( f'*(g/f)+g'*ln f )
        return new Mult(
                new Pow(this.getLeft(), this.getRight()),
                new Plus(
                        new Mult(this.getLeft().differentiate(var), new Div(this.getRight(), this.getLeft())),
                        new Mult(this.getRight().differentiate(var), new Log(Const.E, this.getLeft()))
                )
        );

    }

    @Override
    public Expression simplify() {
        Expression leftSimple = this.getLeft().simplify();
        Expression rightSimple = this.getRight().simplify();
        Double leftValue = BaseExpression.constValueOrNull(leftSimple);
        Double rightValue = BaseExpression.constValueOrNull(rightSimple);

        // x^0 = 1
        if (BaseExpression.equalsAppr(rightValue, 0d)) {
            return new Num(1d);
        }
        // x^1 = x
        if (BaseExpression.equalsAppr(rightValue, 1d)) {
            return leftSimple;
        }
        // 1^x = 1
        if (BaseExpression.equalsAppr(leftValue, 1d)) {
            return new Num(1d);
        }
        // both are const
        if (leftValue != null && rightValue != null) {
            try {
                return new Num(this.evaluate());
            } catch (Exception ex) {
                // do not simplify if it causes an exception to occur.
                ex.toString(); // empty block will cause checkstyle to complain
            }
        }

        Expression r = this.inst(leftSimple, rightSimple);
        // if (this.left != leftSimple || this.right != rightSimple)r = r.simplify();
        return r;
    }
}
