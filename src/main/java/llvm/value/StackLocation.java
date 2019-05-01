package llvm.value;

import llvm.type.Type;

public class StackLocation implements Value {

    static int increment = 0;
    private int id;
    Type type;

    public StackLocation(Type type)
    {
        this.id = increment;
        increment++;
        this.type = type;
    }

    public String getId()
    {
        return Integer.toString(id);
    }

    public String getString()
    {
        return ("%u" + this.id);
    }

    public Type getType()
    {
        return this.type;
    }
}
