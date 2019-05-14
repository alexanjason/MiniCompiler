package llvm.inst;

import arm.Str;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Store implements Instruction {

    // store <ty> <value>, <ty>* <pointer>

    Value ptr;
    Value value;

    public Store(Value value, Value ptr)
    {
        this.ptr = ptr;
        this.value = value;
    }

    public String getString()
    {
        String valTypeStr = ptr.getType().getString();

        return ("store " + valTypeStr + " " + value.getString() + ", " + valTypeStr + "* " + ptr.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        // TODO handle immediate
        Register ptrReg = (Register) ptr;
        Register valueReg = (Register) value;
        list.add(new Str(valueReg, ptrReg));
        return list;
    }
}
