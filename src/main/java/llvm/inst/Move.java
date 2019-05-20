package llvm.inst;

import arm.Mov;
import llvm.value.Local;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Move implements Instruction {

    // TODO hack for Phi

    Local result;
    Value ptr;

    public Move(Local result, Value ptr)
    {
        this.result = result;
        this.ptr = ptr;
    }

    public String getString()
    {
        return "";
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(result, ptr));
        return list;
    }
}
