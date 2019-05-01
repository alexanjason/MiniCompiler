package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Mult implements Instruction {

    Value result;
    Value left;
    Value right;

    public Mult(Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;
    }

    public String getString()
    {
        return (result.getString() + " = mul " + result.getType().getString() + " " +
                left.getString() + ", " + right.getString());
    }
}
