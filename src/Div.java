
/**
 * Division.
 *
 * @author me
 */
public class Div extends BinaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param dividend dividend
     * @param divisor divisor
     */
    public Div(Expression dividend, Expression divisor) {
        super(dividend, divisor);
    }

    @Override
    protected BinaryExpression inst(Expression left, Expression right) {
        return new Div(left, right);
    }

    @Override
    protected double run(double left, double right) {
        return left / right;
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + " / " + this.getRight() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        // (u/v)' = ( u'v - uv' )/( v^2 )
        boolean rightR = this.getRight().getVariables().indexOf(var) == -1;
        if (rightR) {
            // (u/c)' = u'/c
            return new Div(this.getLeft().differentiate(var), this.getRight());
        }
        return new Div(
                new Minus(
                        new Mult(this.getLeft().differentiate(var), this.getRight()),
                        new Mult(this.getLeft(), this.getRight().differentiate(var))
                ),
                new Pow(this.getRight(), new Num(2))
        );
    }

    @Override
    public Expression simplify() {
        Expression leftSimple = this.getLeft().simplify();
        Expression rightSimple = this.getRight().simplify();
        Double leftValue = BaseExpression.constValueOrNull(leftSimple);
        Double rightValue = BaseExpression.constValueOrNull(rightSimple);
        while ((leftSimple instanceof Neg) && (rightSimple instanceof Neg)) {
            leftSimple = ((Neg) leftSimple).getArg();
            rightSimple = ((Neg) rightSimple).getArg();
        }

        // 0/x = 0
        if (BaseExpression.equalsAppr(leftValue, 0d)) {
            return new Num(0d);
        }
        // x/1 = x
        if (BaseExpression.equalsAppr(rightValue, 1d)) {
            return leftSimple;
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
        // x/x = 1
        if (leftSimple.compare(rightSimple)) {
            return new Num(1);
        }
        // (a*x)/x = a
        if ((leftSimple instanceof Mult)) {
            if (rightSimple.compare(((Mult) leftSimple).getLeft())) {
                return ((Mult) leftSimple).getRight();
            }
            if (rightSimple.compare(((Mult) leftSimple).getRight())) {
                return ((Mult) leftSimple).getLeft();
            }
        }
        // (x^a)/(x^b)=x^(a-b) - not implemented

        // if (leftSimple.compare(rightSimple)) {return new Pow(leftSimple, new Num(2));}
        Expression r = this.inst(leftSimple, rightSimple);
        // if (this.left != leftSimple || this.right != rightSimple)r = r.simplify();
        return r;
    }
}
