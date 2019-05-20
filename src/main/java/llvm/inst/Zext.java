package llvm.inst;

import arm.Mov;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Zext implements Instruction {

    Value outValue;
    Value inValue;

    public Zext(Value inValue, Value outValue)
    {
        this.outValue = outValue;
        this.inValue = inValue;
    }

    public String getString()
    {
        return (outValue.getString() + " = zext " + inValue.getType().getString() + " " + inValue.getString() + " to " + outValue.getType().getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register outReg = (Register) outValue;

        list.add(new Mov(outReg, inValue));

        return list;
    }
}
