package llvm.type;

public class i1 implements Type {
    public String getString()
    {
        return ("i1");
    }

    public String getDefault()
    {
        return "0";
    }

    public int getSize()
    {
        return 1;
    }

}
