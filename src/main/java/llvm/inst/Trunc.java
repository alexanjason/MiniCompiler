package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Trunc implements Instruction {
    //Type inType;
    //Type outType;
    Value outValue;
    Value inValue;

    public Trunc(Value inValue, Value outValue)
    {
        //this.inType = inType;
        //this.outType = outType;
        this.outValue = outValue;
        this.inValue = inValue;
    }

    public String getString()
    {
        return (outValue.getString() + " = trunc " + inValue.getType().getString() + " " + inValue.getString() + " to " + outValue.getType().getString());
    }
}
