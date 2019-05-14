package llvm.inst;

import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Getelementptr implements Instruction {

    Value result;
    Value base;
    int index;

    public Getelementptr(Value result, Value base, int index)
    {
        this.result = result;
        this.base = base;
        this.index = index;
    }

    public String getString()
    {
        String typeStr = base.getType().getString();
        String typeStrNotPtr = typeStr.substring(0, typeStr.length() - 1);
        return (result.getString() + " = getelementptr " + typeStrNotPtr + ", " + typeStr + " " +
        base.getString() + ", i1 0, i32 " + index);
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register resultReg = (Register) result;
        Register baseReg = (Register) base;
        list.add(new arm.Add(resultReg, baseReg, new Immediate(Integer.toString(index*4), new i32())));
        return list;
    }
}
