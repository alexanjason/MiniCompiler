package ast.type;

// TODO keep this or no?
public class ErrorType implements Type {
    public boolean compareType(Type type)
    {
        return type instanceof ErrorType;
    }


}
