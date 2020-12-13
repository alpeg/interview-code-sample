
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author me
 */
public class ExpressionsTestExtended {

    /**
     * Throws an AssertionError if given parameter is not true.
     *
     * @param value assertion
     */
    public static void assertTrue(boolean value) {
        if (!value) {
            throw new AssertionError();
        }
    }

    /**
     * Constructor.
     *
     * @param debug print debugging information
     */
    public ExpressionsTestExtended(boolean debug) {
        this.debugPrint = debug;
    }
    private boolean debugPrint;

    /**
     * Prints string if debugging is enabled.
     *
     * @param s string
     */
    public void println(String s) {
        if (this.debugPrint) {
            System.out.println("  " + s);
        }
    }

    /**
     * Throws an AssertionError if given parameter are not approximately equal.
     *
     * @param a a
     * @param b b
     */
    public static void assertEq(Double a, Double b) {
        if (a == null || b == null) {
            assertTrue(Objects.equals(a, b));
            return;
        }
        assertTrue(Math.abs(a - b) < 0.000001);
    }
    // public Double v; public void vset(Double v_) {this.v = v_;} public void veq(Double v_) {eq(this.v, v_);}

    /**
     * Checks if two expressions return equal values by calculating them with
     * random values multiple times - so, x+x would be considered equal to 2x.
     *
     * @param e1 e1
     * @param e2 e2
     * @throws Exception Exception
     */
    public void randomVarEqual(Expression e1, Expression e2) throws Exception {
        Set<String> s = new HashSet<>();
        s.addAll(e1.getVariables());
        s.addAll(e2.getVariables());
        List<String> vars = new ArrayList<>(s.size());
        vars.addAll(s);
        Collections.sort(vars);
        Map<String, Double> varsMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            for (String var : s) {
                double r = this.rand.nextDouble() * 10;
                varsMap.put(var, r);
            }
            try {
                assertEq(e1.evaluate(varsMap), e2.evaluate(varsMap));
            } catch (AssertionError ex) {
                System.out.println("e1 = " + e1);
                System.out.println("e2 = " + e2);
                for (Map.Entry<String, Double> entry : varsMap.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());
                }
                throw ex;
            }
        }
    }

    /**
     * Given three parameters, check if simplifying them preserves returned
     * value when checked by randomVarEqual.
     *
     * @param e e
     * @param de e derivative (automatically calculated)
     * @param e2 e derivative (calculated by hand)
     * @throws Exception Exception
     */
    public void testSimplifyEquality(Expression e, Expression de, Expression e2) throws Exception {
        Expression eSimple = e.simplify();
        Expression deSimple = de.simplify();
        Expression e2Simple = e2.simplify();
        this.randomVarEqual(e, eSimple);
        this.randomVarEqual(de, deSimple);
        this.randomVarEqual(e2, e2Simple);
        this.randomVarEqual(de, e2Simple);
        this.randomVarEqual(deSimple, e2Simple);
        this.randomVarEqual(deSimple, e2);
        Expression eSimpleD = eSimple.differentiate("x");
        this.randomVarEqual(eSimpleD, de);
        this.randomVarEqual(eSimpleD, deSimple);
        this.randomVarEqual(eSimpleD, e2);
        this.randomVarEqual(eSimpleD, e2Simple);
        Expression dd1 = de.differentiate("x");
        Expression dd2 = e2.differentiate("x");
        Expression dd1simple = dd1.simplify();
        Expression dd2simple = dd2.simplify();
        this.randomVarEqual(dd1, dd2);
        this.randomVarEqual(dd1simple, dd2);
        this.randomVarEqual(dd1, dd2simple);
        this.randomVarEqual(dd1simple, dd2simple);
    }

    private Random rand;

    /**
     * Perform a test 2.
     *
     * @throws Exception Exception
     */
    public void test2() throws Exception {
        this.rand = new Random(0L);
        this.println("Testing: Differentiation");
        Expression e;
        Expression de;
        Expression e2;
        // (x^4)'=4x^3
        e = new Pow(new Var("x"), new Num(4));
        de = e.differentiate("x");
        e2 = new Mult(new Num(4), new Pow(new Var("x"), new Num(3)));
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // x'=1
        e = new Var("x");
        de = e.differentiate("x");
        e2 = new Num(1);
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // (xc)' = c'=0
        e = new Mult(new Var("x"), new Var("c"));
        de = e.differentiate("x");
        e2 = new Var("c");
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // c'=0
        e = new Var("c");
        de = e.differentiate("x");
        e2 = new Num(0);
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // 13'=0
        e = new Num(13).differentiate("x");
        de = e.differentiate("x");
        e2 = new Num(0);
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // sin(cx)'=c cos(x)
        e = new Sin(new Mult(new Var("x"), new Var("c")));
        de = e.differentiate("x");
        e2 = new Mult(new Var("c"), new Cos(new Mult(new Var("c"), new Var("x"))));
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // (sin(cx^2))'=2cx*сos(cx^2)
        e = new Sin(new Mult(new Pow(new Var("x"), new Num(2)), new Var("c")));
        de = e.differentiate("x");
        e2 = new Mult(
                new Mult(new Num(2), new Mult(new Var("c"), new Var("x"))),
                new Cos(new Mult(new Pow(new Var("x"), new Num(2)), new Var("c")))
        );
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // (cos(cx^2))'=2cx*(-sin(cx^2))
        e = new Cos(new Mult(new Var("x"), new Var("c")));
        de = e.differentiate("x");
        e2 = new Mult(new Var("c"),
                new Neg(new Sin(new Mult(new Var("x"), new Var("c"))))
        );
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // (x^x)'=(x^x)*(ln(x)+1)
        e = new Pow(new Var("x"), new Var("x"));
        de = e.differentiate("x");
        e2 = new Mult(
                new Pow(new Var("x"), new Var("x")),
                new Plus(new Log(Const.E, new Var("x")), new Num(1))
        );
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // sin(x^x)'= (x^x)*(ln(x)+1)*cos(x^x)
        e = new Sin(new Pow(new Var("x"), new Var("x")));
        de = e.differentiate("x");
        e2 = new Mult(
                new Mult(
                        new Pow(new Var("x"), new Var("x")),
                        new Plus(new Log(Const.E, new Var("x")), new Num(1))
                ),
                new Cos(new Pow(new Var("x"), new Var("x")))
        );
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // ( 1/(1+2x^4) )' = -(8x^3)/((2x^4+1)^2)
        e = new Div(
                new Num(1),
                new Plus(
                        new Num(1),
                        new Mult(new Num(2), new Pow(new Var("x"), new Num(4)))
                )
        );
        de = e.differentiate("x");
        e2 = new Neg(new Div(
                new Mult(new Num(8), new Pow(new Var("x"), new Num(3))),
                new Pow(
                        new Plus(
                                new Mult(new Num(2), new Pow(new Var("x"), new Num(4))),
                                new Num(1)
                        ),
                        new Num(2)
                )
        ));
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        // ( 1/(1+2*(x)^(3x)) )' = -(2x)^(3x) * ( 3 + 3*ln(2x) ) / (1 + (2x)^(3x) )^2
        e = new Div(
                new Num(1),
                new Plus(
                        new Num(1),
                        new Mult(new Num(2),
                                new Pow(
                                        // new Mult(new Num(2), new Var("x")),
                                        new Var("x"),
                                        new Mult(new Num(3), new Var("x"))
                                ))
                )
        );
        de = e.differentiate("x");
        e2 = new Neg(new Div(
                new Mult(
                        new Mult(
                                new Num(6), new Pow(new Var("x"), new Mult(new Num(3), new Var("x")))
                        ),
                        new Plus(new Log(Const.E, new Var("x")), new Num(1))
                ),
                new Pow(
                        new Plus(
                                new Num(1),
                                new Mult(
                                        new Num(2),
                                        new Pow(
                                                new Var("x"),
                                                new Mult(new Num(3), new Var("x"))
                                        )
                                )
                        ),
                        new Num(2)
                )
        ));
        println("diff by x: " + e);
        println("  result: " + de);
        println("  result_simple: " + de.simplify());
        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
        println("  result is equals to: " + e2);

        println("ok1");

        Expression num13 = new Num(13d);

    }

    /**
     * Perform a test 3.
     *
     * @throws Exception Exception
     */
    public void test3() throws Exception {

        Expression e;
        Expression de;
        Expression e2;

        e = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        de = e.differentiate("x");
        e2 = new Mult(new Num(2), new Plus(new Var("x"), new Var("y")));
        println("// " + e); // ((x + y)^2)
        println(de.toString());
        // the result is:
        // (((x + y) ^ 2.0) * (((1.0 + 0.0) * (2.0 / (x + y))) + (0.0 * log(e, (x + y)))))
        println(de.simplify().toString());
        // the result is:
        // (((x + y) ^ 2.0) * (2.0 / (x + y))) = (2 * (x + y))

        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);

        e = new Pow(Const.E, new Var("x"));
        de = e.differentiate("x");
        e2 = new Pow(Const.E, new Var("x"));
        println("// " + e); // (e^x)
        println(e.differentiate("x").toString());
        // ((e ^ x) * ((0.0 * (x / e)) + (1.0 * log(e, e))))
        println(e.differentiate("x").simplify().toString());
        // (e ^ x)

        this.randomVarEqual(de, e2);
        this.testSimplifyEquality(e, de, e2);
    }

    /**
     * Perform a test 1.
     *
     * @throws Exception Exception
     */
    public void test1() throws Exception {
        // Expression e2 = new Pow(new Plus(new Var("x"), new Var("y")), new Num(2));
        println("Testing: Num, Var");
        Expression e2 = new Var("x");
        println("x -> " + e2);
        Expression num2 = new Num(2d);
        println("2 -> " + num2);
        assertTrue(e2.assign("?", e2).equals(e2));
        assertTrue(e2.assign("x", num2).equals(num2));
        assertTrue(e2.assign("x", num2).evaluate() == 2d);
        Map<String, Double> m = new HashMap<>();
        m.put("x", 2d);
        assertTrue(e2.evaluate(m) == 2d);
        assertTrue(e2.toString().equals("x"));
        assertTrue(e2.assign("x", num2).toString().equals("2"));

        Expression num1 = new Num(1d);
        Expression num0 = new Num(0d);

        println("Testing: Plus");
        Expression num3 = new Num(3d);
        e2 = new Plus(new Var("x"), new Var("y"));
        println("x+y -> " + e2);
        assertTrue(e2.assign("x", num2).assign("y", num3).evaluate() == 2d + 3d);
        println("2+3 -> " + e2.assign("x", num2).assign("y", num3));
        m.put("y", 3d);
        assertTrue(e2.evaluate(m) == 2d + 3d);

        println("Testing: Minus");
        e2 = new Minus(new Var("x"), new Var("y"));
        println("x-y -> " + e2);
        assertTrue(e2.assign("x", num2).assign("y", num3).evaluate() == 2d - 3d);
        assertTrue(e2.evaluate(m) == 2d - 3d);

        println("Testing: Pow");
        e2 = new Pow(new Var("x"), new Var("y"));
        println("x^y -> " + e2);
        assertTrue(e2.assign("x", num2).assign("y", num3).evaluate() == Math.pow(2d, 3d));
        assertTrue(e2.evaluate(m) == Math.pow(2d, 3d));

        println("Testing: Neg");
        e2 = new Neg(new Var("x"));
        println("-x -> " + e2);
        assertTrue(e2.assign("x", num2).assign("y", num3).evaluate() == -2d);
        assertTrue(e2.evaluate(m) == -2d);

        println("Testing: Sin");
        e2 = new Sin(num2);
        println("sin 2 -> " + e2);
        assertTrue(e2.evaluate() == Math.sin(2d));
        println("Testing: Cos");
        e2 = new Cos(num2);
        println("cos 2 -> " + e2);
        assertTrue(e2.evaluate() == Math.cos(2d));
        println("Testing: Mult");
        e2 = new Mult(num2, num3);
        println("2*3 -> " + e2);
        assertTrue(e2.evaluate() == 2d * 3d);
        println("Testing: Div");
        e2 = new Div(num2, num3);
        println("2/3 -> " + e2);
        assertTrue(e2.evaluate() == 2d / 3d);
        assertTrue(!Double.isFinite(new Div(num2, num0).evaluate()));
        println("Testing: Log");
        e2 = new Log(num3, new Num(9));
        println("log_3 9 -> " + e2);
        assertTrue(Math.abs(e2.evaluate() - 2d) < 0.000001); // 2.0000000000000004
        println("Good.");
    }

    /**
     * Run tests.
     *
     * @param debug show debugging text
     */
    public static void runTests(boolean debug) {
        try {
            ExpressionsTestExtended a = new ExpressionsTestExtended(debug);
            a.test1();
            a.println("===");
            a.test2();
            a.println("===");
            a.test3();
        } catch (Throwable t) {
            System.out.println("=== TEST FAILED! ===");
            t.printStackTrace();
        }
    }

    /**
     * Main function.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        runTests(true);
    }

}
