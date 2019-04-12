
import com.ginsberg.junit.exit.ExpectSystemExit;
import mini.MiniCompiler;
import org.junit.*;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.jupiter.api.BeforeEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TypeChecking {


    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    public MiniCompiler mini = new MiniCompiler();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void beforeEachTest()
    {
        System.out.println("***");
    }

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
    @ExpectSystemExit
    public void StructFail()
    {
        exit.expectSystemExit();
        // TODO get this to work
        MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/struct_fail.mini";
        String[] args = {file};
        mini.main(args);
        assertEquals("16: equality operators require same types", errContent.toString());
    }


}
