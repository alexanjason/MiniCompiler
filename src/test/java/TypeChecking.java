
import mini.MiniCompiler;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.*;

public class TypeChecking {


    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    public MiniCompiler mini = new MiniCompiler();

    @BeforeEach public void beforeEachTest()
    {
        System.out.println("***");
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
/*
    @Test
    public void StructFail()
    {
        exit.expectSystemExit();
        // TODO get this to work
        MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/struct_fail.mini";
        String[] args = {file};
        mini.main(args);
    }
*/

}
