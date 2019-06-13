package llvm.type;

public class Void implements Type {

    public String getString()
    {
        return "void";
    }

    public String getDefault()
    {
        return "void";
    }

    public int getSize()
    {
        return 0;
    }
}
