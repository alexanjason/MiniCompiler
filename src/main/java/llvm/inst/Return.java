package llvm.inst;

import arm.Mov;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Return implements Instruction {

    // ret <ty> <value>

    Value value;

    public Return(Value value)
    {
        this.value = value;
    }

    public String getString()
    {
        return ("ret " + value.getType().getString() + " " + value.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(new Register(value.getType(), 0), value));
        return list;
    }
}
