package ast.type;

public class BoolType implements Type
{
    public boolean compareType(Type type)
    {
        return type instanceof BoolType;
    }

}
