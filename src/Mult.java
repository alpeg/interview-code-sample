
/**
 *
 * @author me
 */
public class Mult extends BinaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param multiplier multiplier
     * @param multiplicand multiplicand
     */
    public Mult(Expression multiplier, Expression multiplicand) {
        super(multiplier, multiplicand);
    }

    @Override
    protected BinaryExpression inst(Expression left, Expression right) {
        return new Mult(left, right);
    }

    @Override
    protected double run(double left, double right) {
        return left * right;
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + " * " + this.getRight() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        boolean leftR = this.getLeft().getVariables().indexOf(var) == -1;
        boolean rightR = this.getRight().getVariables().indexOf(var) == -1;
        if (leftR && rightR) { // c*c, c' = 0
            return new Num(0);
        }
        if (leftR && !rightR) { // (cf)'=c(f')
            return new Mult(this.getLeft(), this.getRight().differentiate(var));
        }
        if (!leftR && rightR) { // (fc)'=(f')c
            return new Mult(this.getLeft().differentiate(var), this.getRight());
        }
        // (fg)'(x) = f'(x)g(x) + f(x)g'(x)
        return new Plus(
                new Mult(this.getLeft().differentiate(var), this.getRight()),
                new Mult(this.getLeft(), this.getRight().differentiate(var))
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

        // 0*x, x*0
        if (BaseExpression.equalsAppr(leftValue, 0d) || BaseExpression.equalsAppr(rightValue, 0d)) {
            return new Num(0d);
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
        // 1*x, x*1
        if (BaseExpression.equalsAppr(leftValue, 1d)) {
            return rightSimple;
        }
        if (BaseExpression.equalsAppr(rightValue, 1d)) {
            return leftSimple;
        }
        // if (leftSimple.compare(rightSimple)) {return new Pow(leftSimple, new Num(2));}
        Expression r = this.inst(leftSimple, rightSimple);
        // if (this.left != leftSimple || this.right != rightSimple)r = r.simplify();
        return r;
    }

    @Override
    protected boolean orderMatters() {
        return false;
    }
}
