package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Sdiv implements Instruction {

    Value result;
    Value left;
    Value right;
    //Type type;

    public Sdiv(Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;
        //this.type = type;
    }

    public String getString()
    {
        return (result.getString() + " = sdiv " + result.getType().getString() + " " + left.getString()
                + ", " + right.getString());
    }
}
