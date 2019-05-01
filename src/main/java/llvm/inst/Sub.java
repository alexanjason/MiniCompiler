package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Sub implements Instruction {

    Value result;
    Value left;
    Value right;

    public Sub (Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;
    }

    public String getString()
    {
        return (result.getString() + " = sub " + result.getType().getString() + " " + left.getString() + ", " + right.getString());
    }
}
