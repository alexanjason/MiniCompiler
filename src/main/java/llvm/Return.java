package llvm;

public class Return implements Instruction {

    // ret <ty> <value>

    ast.type.Type type;
    String value;

    public Return(ast.type.Type type, String value)
    {
        this.type = type;
        this.value = value;
    }
}
