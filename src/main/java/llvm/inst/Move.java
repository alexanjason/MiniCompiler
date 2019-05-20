package llvm.inst;

import arm.Mov;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Move implements Instruction {

    Value result;
    Value ptr;

    public Move(Register result, Value ptr)
    {
        this.result = result;
        this.ptr = ptr;
    }

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
        if (result instanceof Local)
        {
            list.add(new Mov((Local)result, ptr));
        }
        else if (result instanceof Register)
        {
            list.add(new Mov((Register)result, ptr));
        }
        return list;
    }
}
