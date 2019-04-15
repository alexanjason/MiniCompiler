package llvm;

public class Allocate implements Instruction {

    // %<name>= alloca <type>

    private String name;
    private ast.type.Type type;

    public Allocate(String name, ast.type.Type type)
    {
        this.name = name;
        this.type = type;
    }

    public String toString()
    {
        return ("%" + name + " = alloca " + type.getLLVM());
    }
}
