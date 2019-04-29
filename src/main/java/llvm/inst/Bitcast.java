package llvm.inst;

import llvm.inst.Instruction;
import llvm.type.Type;
import llvm.value.Value;

public class Bitcast implements Instruction {

    //Type from;
    //Type to;
    Value val;
    Value result;

    public Bitcast(Value val, Value result)
    {
        //this.from = from;
        //this.to = to;
        this.val = val;
        this.result = result;
    }

    public String getString()
    {
        return (result.getString() + " = bitcast " + val.getType().getString() + " " + val.getString() +
                " to " + result.getType().getString());
    }
}
