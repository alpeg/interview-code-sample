
/**
 * Sine function.
 *
 * @author me
 */
public class Sin extends UnaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param arg argument
     */
    public Sin(Expression arg) {
        super(arg);
    }

    @Override
    protected UnaryExpression inst(Expression arg) {
        return new Sin(arg);
    }

    @Override
    protected double run(double arg) {
        return Math.sin(arg);
    }

    @Override
    public String toString() {
        return "sin(" + this.getArg() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        // (sin u)' = cos u * u'
        return new Mult(new Cos(this.getArg()), this.getArg().differentiate(var));
    }

    @Override
    public Expression simplify() {
        return this;
    }
}
