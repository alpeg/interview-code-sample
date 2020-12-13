
import java.util.HashMap;
import java.util.Map;

/**
 * Expression test.
 *
 * @author me
 */
public class ExpressionsTest {

    /**
     * Main function.
     *
     * @param args the command line arguments
     * @throws Exception Exception
     */
    public static void main(String[] args) throws Exception {
        // 1. Create the expression (2*x) + (sin(4*y)) + (e^x).
        Expression e = new Plus(new Plus(
                new Mult(new Num(2), new Var("x")),
                new Sin(new Mult(new Num(4), new Var("y")))
        ), new Pow(Const.E, new Var("x"))); // Const.E = new Const("e", Math.E)
        // 2. Print the expression.
        System.out.println(e); // (((2 * x) + sin((4 * y))) + (e^x))
        // 3. Print the value of the expression with (x=2,y=0.25,e=2.71).
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 2d);
        vars.put("y", 0.25);
        System.out.println(e.evaluate(vars)); // 12.230527083738547
        // 4. Print the differentiated expression according to x.
        Expression de = e.differentiate("x");
        System.out.println(de); // (((2 * 1) + (сos((4 * y)) * 0)) + (((e^x) * log(e, e)) * 1))
        // 5. Print the value of the differentiated expression according to x with the assignment above.
        System.out.println(de.evaluate(vars)); // 9.389056098930649
        // 6. Print the simplified differentiated expression.
        System.out.println(de.simplify()); // (2 + (2.718281828459045^x))
    }
}
