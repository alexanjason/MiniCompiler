package ast.type;

public class NullType implements Type{
    public boolean compareType(Type type)
    {
        return (type instanceof NullType || type instanceof StructType);
    }
}
