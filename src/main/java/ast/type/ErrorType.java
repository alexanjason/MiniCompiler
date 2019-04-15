package ast.type;

// TODO keep this or no?
public class ErrorType implements Type {
    public boolean compareType(Type type)
    {
        return type instanceof ErrorType;
    }

    public String getLLVM()
    {
        System.err.println("Error type in CFG");
        System.exit(8);
        return "Error";
    }

}
