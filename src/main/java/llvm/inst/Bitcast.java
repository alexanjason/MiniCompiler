package llvm.inst;

import arm.Mov;
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
        System.err.println("sscpinit bitcast. result: " + result.getString() + "val: " + val.getString());
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval allocate");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (val == v)
        {
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
