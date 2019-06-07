package llvm.inst;

import arm.Str;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Store implements Instruction {

    // store <ty> <value>, <ty>* <pointer>

    Value ptr;
    Value value;

    public Store(Value value, Value ptr)
    {
        this.ptr = ptr;
        this.value = value;

        // TODO def?
        ptr.addUse(this);
        value.addUse(this);
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (ptr == v)
        {
            ptr.getUses().remove(this);
            ptr = constant;
        }
    }

    public void replace(Value oldV, Value newV)
    {
        if (ptr == oldV)
        {
            ptr.getUses().remove(this);
            ptr = newV;
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public boolean checkRemove(ListIterator list)
    {
        // TODO ?
        if (ptr.isMarked())
        {
            list.remove();
            return true;
        }
        return false;
    }

    public String getString()
    {
        String valTypeStr = ptr.getType().getString();

        return ("store " + valTypeStr + " " + value.getString() + ", " + valTypeStr + "* " + ptr.getString());
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        // TODO def?
        System.err.println("sscpinit store");
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval store");
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
