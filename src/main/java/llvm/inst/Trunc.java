package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Trunc implements Instruction {

    Value outValue;
    Value inValue;

    public Trunc(Value inValue, Value outValue)
    {
        this.outValue = outValue;
        this.inValue = inValue;
    }

    public String getString()
    {
        return (outValue.getString() + " = trunc " + inValue.getType().getString() + " " + inValue.getString() + " to " + outValue.getType().getString());
    }
}
