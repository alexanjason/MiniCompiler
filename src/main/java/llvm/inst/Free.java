package llvm.inst;

import arm.Bl;
import arm.Mov;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Free implements Instruction {

    Value ptr;

    public Free(Value ptr)
    {
        this.ptr = ptr;

        ptr.addUse(this);
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
        return false;
    }

    public String getString()
    {
        return ("call void @free(i8* " + ptr.getString() + ")");
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
    }

    public void sscpReplace(Value v, Immediate constant)
    {
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();

        list.add(new Mov(new Register(ptr.getType(), 0), ptr));
        list.add(new Bl("free"));

        return list;
    }

}
