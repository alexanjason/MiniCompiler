package llvm.type;

public class i8 implements Type {

    public String getString()
    {
        return "*i8"; // TODO sloppy
    }

    public int getSize()
    {
        return 8;
    }

    public String getDefault()
    {
        return "0";
    }

}
