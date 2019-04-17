package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Load implements Instruction {

    Type type;
    Value result;
    Value pointer;

    public Load(Type type, Value result, Value pointer) {
        this.type = type;
        this.result = result;
        this.pointer = pointer;
    }

    public String getString()
    {
        return (result.getString() + " = load " + type.getString() + ", " + type.getString() + "* " + pointer.getString());
    }
}
