package llvm.value;

import llvm.type.Type;

public class Global implements Value {

    String id;
    Type type;

    public Global(String id, Type type)
    {
        this.id = id;
        this.type = type;
    }

    public String getString()
    {
        return ("@" + id);
    }

    public Type getType()
    {
        return this.type;
    }
}
