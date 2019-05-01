package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Load implements Instruction {

    Value result;
    Value pointer;

    public Load(Value result, Value pointer) {
        this.result = result;
        this.pointer = pointer;
    }

    public String getString()
    {
        String typeStr = result.getType().getString();
        return (result.getString() + " = load " + typeStr + ", " + typeStr + "* " + pointer.getString());
    }
}
