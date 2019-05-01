package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Return implements Instruction {

    // ret <ty> <value>

    Value value;

    public Return(Value value)
    {
        this.value = value;
    }

    public String getString()
    {
        return ("ret " + value.getType().getString() + " " + value.getString());
    }
}
