package llvm.inst;

import llvm.value.Value;
import llvm.type.Type;

public class Or implements Instruction {

    // <result> = or <ty> <op1>, <op2>
    Value result;
    Value op1;
    Value op2;

    public Or(Value result, Value op1, Value op2)
    {
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
    }

    public String getString()
    {
        return (result.getString() + " = or " + result.getType().getString() + " " + op1.getString() + ", " + op2.getString());
    }
}