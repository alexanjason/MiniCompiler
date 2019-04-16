package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Sub implements Instruction {

    Value result;
    Value left;
    Value right;
    Type type;

    public Sub (Value result, Value left, Value right, Type type)
    {
        this.result = result;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return (result.getString() + " = sub " + type.getString() + " " + left.getString() + ", " + right.getString());
    }
}
