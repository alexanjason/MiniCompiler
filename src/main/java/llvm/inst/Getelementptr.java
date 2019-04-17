package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Getelementptr implements Instruction {

    Value result;
    Type type;
    Value base;
    int index;

    public Getelementptr(Value result, Type type, Value base, int index)
    {
        this.result = result;
        this.type = type;
        this.base = base;
        this.index = index;
    }

    public String getString()
    {
        String typeStr = type.getString();
        String typeStrNotPtr = typeStr.substring(0, typeStr.length() - 1);
        return (result.getString() + " = getelementptr " + typeStrNotPtr + ", " + typeStr + " " +
        base.getString() + ", i1 0, i32 " + index);
    }
}
