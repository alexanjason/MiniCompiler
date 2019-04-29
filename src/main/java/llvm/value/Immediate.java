package llvm.value;

import llvm.type.Type;

public class Immediate implements Value {

    String val;
    Type type;

    public Immediate(String val, Type type)
    {
        this.val = val;
        this.type = type;
    }

    public String getString()
    {
        return val;
    }

    public Type getType()
    {
        return this.type;
    }
}
