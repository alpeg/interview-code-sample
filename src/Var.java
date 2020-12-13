
import java.util.List;
import java.util.Map;

/**
 * Variable.
 *
 * @author me
 */
public class Var implements Expression {

    private String name;

    /**
     * Constructor.
     *
     * @param name variable name
     */
    public Var(String name) {
        this.name = name;
    }

    @Override
    public double evaluate(Map<String, Double> assignment) throws Exception {
        Expression expr = this;
        Double value = assignment.get(this.name);
        if (value == null) {
            throw new Exception("Cannot evaluate unassigned variable " + this.name);
        }
        return this.assign(this.name, new Num(value)).evaluate();
    }

    @Override
    public double evaluate() throws Exception {
        throw new Exception("Cannot evaluate unassigned variable " + this.name);
    }

    @Override
    public List<String> getVariables() {
        return List.of(this.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression assign(String var, Expression expression) {
        return this.name.equals(var) ? expression : this;
    }

    @Override
    public Expression differentiate(String var) {
        // x' = 1, c' = 0
        return this.name.equals(var) ? new Num(1) : new Num(0);
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public boolean compare(Expression other) {
        return (other instanceof Var) && this.name.equals(((Var) other).name);
    }

}
