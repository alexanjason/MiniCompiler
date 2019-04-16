package llvm.type;

public class i32 implements Type {

    public String getString()
    {
        return ("i32");
    }

    public String getDefault()
    {
        return "0";
    }
}
