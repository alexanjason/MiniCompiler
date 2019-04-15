package llvm;

public class Icmp implements Instruction {

    private Value result;
    private Value cond;
    private Type type;
    private Value op1;
    private Value op2;

    public Icmp(Value result, Value cond, Type type, Value op1, Value op2) {
        this.result = result;
        this.cond = cond;
        this.type = type;
        this.op1 = op1;
        this.op2 = op2;
    }

    public String getString()
    {
        return (result.getString() + " = icmp " + cond.getString() + " " +
                type.getString() + " " + op1.getString() + " " + op2.getString());
    }
}
