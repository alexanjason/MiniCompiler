package llvm.type;

public class Struct implements Type {

    String name;
    int size;

    public Struct(String name, int size)
    {
        this.name = name;
        this.size = size;
    }

    public String getString()
    {
        return ("%struct." + name + "*");
    }

    public String getDefault()
    {
        return "null";
    }

    public int getSize()
    {
        return size;
    }
}
