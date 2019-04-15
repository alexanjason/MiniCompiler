package llvm;

public class Struct implements Type {

    String name;

    public Struct(String name)
    {
        this.name = name;
    }

    public String getString()
    {
        return ("struct." + name);
    }
}
