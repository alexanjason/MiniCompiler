package llvm.inst;

import llvm.value.Value;
import llvm.type.Type;

public class And implements Instruction {

    // <result> = and <ty> <op1>, <op2>
    Value result;
    //Type type;
    Value op1;
    Value op2;

    public And(Value result, Value op1, Value op2)
    {
        this.result = result;
        //this.type = type;
        this.op1 = op1;
        this.op2 = op2;
    }

    public String getString()
    {
        return (result.getString() + " = and " + result.getType().getString() + " " + op1.getString() + ", " + op2.getString());
    }
}
