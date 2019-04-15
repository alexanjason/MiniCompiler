package ast.type;

public class IntType implements Type
{
    public boolean compareType(Type type)
    {
        return type instanceof IntType;
    }

    public String getLLVM()
    {
        return "i32";
    }
}
