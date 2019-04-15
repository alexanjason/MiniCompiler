package ast.type;

public class VoidType implements Type
{
    public boolean compareType(Type type)
    {
        return type instanceof VoidType;
    }

    public String getLLVM()
    {
        System.err.println("Void type in CFG");
        System.exit(8);
        return "Void";
    }
}
