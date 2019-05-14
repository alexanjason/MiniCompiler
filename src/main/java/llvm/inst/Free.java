package llvm.inst;

import arm.Bl;
import arm.Mov;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Free implements Instruction {

    Value ptr;

    public Free(Value ptr)
    {
        this.ptr = ptr;
    }

    public String getString()
    {
        return ("call void @free(i8* " + ptr.getString() + ")");
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();

        list.add(new Mov(new Register(ptr.getType(), 0), ptr));
        list.add(new Bl("free"));

        return new ArrayList<>();
    }

}


