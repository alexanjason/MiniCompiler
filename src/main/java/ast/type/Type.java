package ast.type;

public interface Type
{
    public boolean compareType(Type type);

    // for instructions
    public String getLLVM();
}
