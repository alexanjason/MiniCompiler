package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Zext implements Instruction {

    Value outValue;
    Value inValue;

    public Zext(Value inValue, Value outValue)
    {
        this.outValue = outValue;
        this.inValue = inValue;
    }

    public String getString()
    {
        return (outValue.getString() + " = zext " + inValue.getType().getString() + " " + inValue.getString() + " to " + outValue.getType().getString());
    }
}
