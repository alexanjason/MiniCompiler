package llvm.value;

public class Local implements Value {

    String id;

    public Local(String id)
    {
        this.id = id;
    }

    public String getString()
    {
        return ("%" + id);
    }
}
