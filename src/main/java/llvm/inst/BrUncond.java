package llvm.inst;

import arm.B;
import cfg.Label;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class BrUncond implements Instruction {

    Label dest;

    public BrUncond(Label dest)
    {
        this.dest = dest;
    }

    public void replace(Value oldV, Value newV)
    {
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public String getString()
    {
        return ("br label %" + dest.getString());
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
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
        list.add(new B(dest));
        return list;
    }
}
