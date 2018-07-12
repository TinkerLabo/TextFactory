package kyPkg.jython;
import org.python.util.PythonInterpreter;
import org.python.core.*;
public class JythonTest {
    public static void test02() throws PyException {
        PythonInterpreter interp = new PythonInterpreter();
        System.out.println("test02");
        interp.exec("from turtle import *");
        interp.exec("forward(100)");
        interp.exec("right(120)");
        interp.exec("forward(100)");
        interp.exec("right(120)");
        interp.exec("forward(100)");
        interp.exec("right(60)");
        interp.exec("forward(100)");
        interp.exec("right(120)");
        interp.exec("forward(100)");
        interp.exec("left(120)");
        interp.exec("forward(100)");
        interp.exec("left(120)");
        interp.exec("forward(200)");
        interp.exec("left(120)");
        interp.exec("forward(100)");
        System.out.println("test02 end");
    }

	public static void test01() throws PyException {
        PythonInterpreter interp = new PythonInterpreter();
        System.out.println("Hello, brave new world");
        interp.exec("import sys");
        interp.exec("print sys");
        interp.exec("import recommendations");
        interp.exec("print recommendations");
        interp.set("a", new PyInteger(42));
        interp.exec("print a");
        interp.exec("x = 2+2");
        PyObject x = interp.get("x");
        System.out.println("x: "+x);
        System.out.println("Goodbye, cruel world");
    }
    public static void main(String []args) {
    	test02();
    }

}
