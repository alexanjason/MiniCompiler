package llvm.type;

public class Struct implements Type {

    String name;

    public Struct(String name)
    {
        this.name = name;
    }

    public String getString()
    {
        return ("%struct." + name + "*");
    }

    public String getDefault()
    {
        return "null";
    }
}
