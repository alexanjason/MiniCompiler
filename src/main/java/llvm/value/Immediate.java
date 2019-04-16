package llvm.value;

public class Immediate implements Value {

    String val;

    public Immediate(String val)
    {
        this.val = val;
    }

    public String getString()
    {
        return val;
    }
}
