package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Trunc implements Instruction {
    Type inType;
    Type outType;
    Value outValue;
    Value inValue;

    public Trunc(Type inType, Value inValue, Type outType, Value outValue)
    {
        this.inType = inType;
        this.outType = outType;
        this.outValue = outValue;
        this.inValue = inValue;
    }

    public String getString()
    {
        return (outValue.getString() + " = trunc " + inType.getString() + " " + inValue.getString() + " to " + outType.getString());
    }
}
