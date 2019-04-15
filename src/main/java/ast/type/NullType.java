package ast.type;

public class NullType implements Type{
    public boolean compareType(Type type)
    {
        return (type instanceof NullType || type instanceof StructType);
    }

    public String getLLVM()
    {
        System.err.println("Null type in CFG");
        System.exit(8);
        return "Null";
    }
}
