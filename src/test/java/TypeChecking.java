import mini.MiniCompiler;
import org.junit.*;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/*
ERROR CODES

1) no main
2) redeclaration
3) return error
4) type mismatch
5) non boolean guard
6) not found
7) incorrect invocation
*/

public class TypeChecking {


    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    public MiniCompiler mini = new MiniCompiler();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @Before public void before()
    {
        System.setErr(new PrintStream(errContent));
    }

    @After public void after()
    {
        System.setErr(originalErr);
    }

    @Test
    public void NoError()
    {
        String file = "typechecking/1.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void StructPass()
    {
        String file = "typechecking/struct_pass.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void StructFail()
    {
        exit.expectSystemExitWithStatus(4);
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/struct_fail.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void ReturnTypeFailDotExp()
    {
        exit.expectSystemExitWithStatus(3);
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_1.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void NonBoolGuardFailCondStmt()
    {
        exit.expectSystemExitWithStatus(5);
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_2.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void ReturnTypeFail()
    {
        exit.expectSystemExitWithStatus(3);
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_3.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void CompOpTypeFail()
    {
        exit.expectSystemExitWithStatus(4);
        MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_4.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void TooManyArgsFail()
    {
        exit.expectSystemExitWithStatus(7);
        MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_5.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void WrongTypeArgFail()
    {
        exit.expectSystemExitWithStatus(4);
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_6.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void DotFail()
    {
        exit.expectSystemExitWithStatus(4);
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2_7.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void Fixed2Pass()
    {
        //MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2.mini";
        String[] args = {file};
        mini.main(args);
    }


}
