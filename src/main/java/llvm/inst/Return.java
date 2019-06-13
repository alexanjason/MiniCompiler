package llvm.inst;

import arm.Mov;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Return implements Instruction {

    // ret <ty> <value>

    Value value;

    public Return(Value value)
    {
        this.value = value;

        value.addUse(this);
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit return");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (value == v)
        {
            value.getUses().remove(this);
            Immediate newImm = new Immediate(constant.getId(), value.getType());
            value = newImm;
        }
    }

    public void replace(Value oldV, Value newV)
    {
        if (value == oldV)
        {
            value.getUses().remove(this);
            value = newV;
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
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
