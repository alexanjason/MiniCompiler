
import mini.MiniCompiler;
import org.junit.jupiter.api.*;

public class TypeChecking {
    @BeforeEach public void beforeEachTest()
    {
        System.out.println("***");
    }

    @Test
    public void NoError()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "typechecking/2.mini";
        String[] args = {file};
        mini.main(args);
        //TODO actually check what is returned
    }
}
