package llvm;

public class Allocate implements Instruction {

    // %<name>= alloca <type>

    private String name;
    private Type type;

    public Allocate(String name, Type type)
    {
        this.name = name;
        this.type = type;
    }

    public String getString()
    {
        return ("%" + name + " = alloca " + type.getString());
    }
}
