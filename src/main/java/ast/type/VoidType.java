package ast.type;

public class VoidType
   implements Type
{
    public boolean compareType(Type type)
    {
        return type instanceof VoidType;
    }
}
