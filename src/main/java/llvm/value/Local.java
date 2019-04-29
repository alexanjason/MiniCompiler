package llvm.value;

import llvm.type.Type;

public class Local implements Value {

    String id;
    Type type;

    public Local(String id, Type type)
    {
        this.id = id;
        this.type = type;
    }

    public String getString()
    {
        return (id);
    }

    public Type getType()
    {
        return this.type;
    }
}
