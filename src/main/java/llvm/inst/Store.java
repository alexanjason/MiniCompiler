package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Store implements Instruction {

    // store <ty> <value>, <ty>* <pointer>

    Value ptr;
    Value value;
    Type type;

    public Store(Value value, Type type, Value ptr)
    {
        this.ptr = ptr;
        this.value = value;
        this.type = type;
    }

    public String getString()
    {
        return ("store " + type.getString() + " " + value.getString() + ", " + type.getString() + "* " + ptr.getString());
    }

}
