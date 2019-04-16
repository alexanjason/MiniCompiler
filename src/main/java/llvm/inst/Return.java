package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Return implements Instruction {

    // ret <ty> <value>

    Type type;
    Value value;

    public Return(Type type, Value value)
    {
        this.type = type;
        this.value = value;
    }

    public String getString()
    {
        return ("ret " + type.getString() + " " + value.getString());
    }
}
