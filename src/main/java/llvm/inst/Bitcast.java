package llvm.inst;

import arm.Mov;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Bitcast implements Instruction {

    Value val;
    Value result;

    public Bitcast(Value val, Value result)
    {
        this.val = val;
        this.result = result;
    }

    public String getString()
    {
        return (result.getString() + " = bitcast " + val.getType().getString() + " " + val.getString() +
                " to " + result.getType().getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r = (Register) result;
        list.add(new Mov(r, val));
        return list;
    }


}
