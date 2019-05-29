package llvm.inst;

import arm.B;
import arm.Beq;
import arm.Cmp;
import cfg.*;
import llvm.value.*;

import java.util.*;

public class BrCond implements Instruction {

    Value cond;
    Label ifTrue;
    Label ifFalse;

    public BrCond(Value cond, Label ifTrue, Label ifFalse)
    {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;

        cond.addUse(this);
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit brcond");
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval brcond");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (cond == v)
        {
            cond = constant;
        }
        // TODO simplify branch to uncond
    }

    public String getString()
    {
        return ("br i1 " + cond.getString() + ", label %" + ifTrue.getString() + ", label %" + ifFalse.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r = (Register) cond;
        list.add(new Cmp(r, new Immediate("1", cond.getType())));
        list.add(new Beq(ifTrue));
        list.add(new B(ifFalse));
        return list;
    }
}
