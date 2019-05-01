package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Store implements Instruction {

    // store <ty> <value>, <ty>* <pointer>

    Value ptr;
    Value value;

    public Store(Value value, Value ptr)
    {
        this.ptr = ptr;
        this.value = value;
    }

    public String getString()
    {
        String valTypeStr = ptr.getType().getString();

        return ("store " + valTypeStr + " " + value.getString() + ", " + valTypeStr + "* " + ptr.getString());
    }

}
