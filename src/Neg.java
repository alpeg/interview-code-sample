
/**
 *
 * @author me
 */
public class Neg extends UnaryExpression implements Expression {

    /**
     * Constructor.
     *
     * @param arg argument
     */
    public Neg(Expression arg) {
        super(arg);
    }

    @Override
    protected UnaryExpression inst(Expression arg) {
        return new Neg(arg);
    }

    @Override
    protected double run(double arg) {
        return -arg;
    }

    @Override
    public String toString() {
        return "-(" + this.getArg() + ")";
    }

    @Override
    public Expression differentiate(String var) {
        return this.inst(this.getArg().differentiate(var));
    }

    @Override
    public Expression simplify() {
        return this.inst(this.getArg().simplify());
    }
}
