package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

public class Icmp implements Instruction {

    private Value result;
    private String cond;
    private Value op1;
    private Value op2;

    public Icmp(Value result, String cond, Value op1, Value op2) {
        this.result = result;
        this.cond = cond;
        this.op1 = op1;
        this.op2 = op2;
    }

    public String getString()
    {
        return (result.getString() + " = icmp " + cond + " " +
                op1.getType().getString() + " " + op1.getString() + ", " + op2.getString());
    }
}
