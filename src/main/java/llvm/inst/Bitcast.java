package llvm.inst;

import arm.Mov;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Bitcast implements Instruction {

    Value val;
    Value result;

    public Bitcast(Value val, Value result)
    {
        this.val = val;
        this.result = result;

        result.addDef(this);
        val.addUse(this);
    }

    public void replace(Value oldV, Value newV)
    {
        if (val == oldV)
        {
            val.getUses().remove(this);
            val = newV;
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public boolean checkRemove(ListIterator list)
    {
        if (result.isMarked())
        {
            list.remove();
            return true;
        }
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        map.put(result, new SSCPValue.Bottom());
        workList.add(result);
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (val == v)
        {
            val.getUses().remove(this);
            val = constant;
        }
    }

    public String getString()
    {
        return (result.getString() + " = bitcast " + val.getType().getString() + " " + val.getString() +
                " to " + result.getType().getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r = (Register) result;
        list.add(new Mov(r, val));
        return list;
    }
}
