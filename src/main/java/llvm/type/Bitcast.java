package llvm.type;

import llvm.inst.Instruction;
import llvm.value.Value;

public class Bitcast implements Instruction {

    Type from;
    Type to;
    Value val;
    Value result;

    public Bitcast(Type from, Type to, Value val, Value result)
    {
        this.from = from;
        this.to = to;
        this.val = val;
        this.result = result;
    }

    public String getString()
    {
        return (result.getString() + " = bitcast " + from.getString() + " " + val.getString() +
                " to " + to.getString());
    }
}
