package llvm.inst;

import llvm.value.Value;
import llvm.type.Type;

public class Xor implements Instruction {

    Value op1;
    Value op2;
    Value result;

    public Xor(Value op1, Value op2, Value result)
    {
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
    }

    public String getString()
    {
        return (result.getString() + " = xor " + result.getType().getString() + " " + op1.getString() + ", " + op2.getString());
    }
}
