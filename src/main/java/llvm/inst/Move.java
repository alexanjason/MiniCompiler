package llvm.inst;

import arm.Mov;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Move implements Instruction {

    Value result;
    Value ptr;

    public Move(Register result, Value ptr)
    {
        this.result = result;
        this.ptr = ptr;

        //result.addDef(this);
        //ptr.addUse(this);
    }

    public Move(Local result, Value ptr)
    {
        this.result = result;
        this.ptr = ptr;
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (ptr == v)
        {
            ptr = constant;
        }
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval move");
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit move. pointer: " + ptr.getString());
    }

    public String getString()
    {
        return "";
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        if (result instanceof Local)
        {
            list.add(new Mov((Local)result, ptr));
        }
        else if (result instanceof Register)
        {
            list.add(new Mov((Register)result, ptr));
        }
        return list;
    }
}
