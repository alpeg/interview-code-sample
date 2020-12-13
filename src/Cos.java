
/**
 * Cosine function.
 *
 * @author me
 */
public class Cos extends UnaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param arg argument
     */
    public Cos(Expression arg) {
        super(arg);
    }

    @Override
    protected UnaryExpression inst(Expression arg) {
        return new Cos(arg);
    }

    @Override
    protected double run(double arg) {
        return Math.cos(arg);
    }

    @Override
    public String toString() {
        return "сos(" + this.getArg() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        // (cos u)' = -sin u * u'
        return new Neg(new Mult(new Sin(this.getArg()), this.getArg().differentiate(var)));
    }

    @Override
    public Expression simplify() {
        return this;
    }
}
