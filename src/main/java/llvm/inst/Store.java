package llvm.inst;

import arm.Str;
import llvm.value.Immediate;
import llvm.value.Local;
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
        //System.out.println("store value: " + value.getString() + " ptr: " + ptr.getString());

        Register ptrReg = (Register) ptr;
        if (value instanceof Immediate)
        {
            Register valueReg = ImmediateToRegister((Immediate)value, list);
            list.add(new Str(valueReg, ptrReg));
        }
        else if (value instanceof Local)
        {
            list.add(new Str((Local)value, ptrReg));
        }
        else
        {
            list.add(new Str((Register)value, ptrReg));
        }

        return list;
    }
}
