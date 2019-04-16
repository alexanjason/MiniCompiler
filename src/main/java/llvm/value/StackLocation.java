package llvm.value;

public class StackLocation implements Value {

    static int increment = 0;
    private int id;

    public StackLocation()
    {
        this.id = increment;
        increment++;
    }

    public String getString()
    {
        return ("%u" + this.id);
    }
}
