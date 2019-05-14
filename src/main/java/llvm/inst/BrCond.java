package llvm.inst;

import arm.B;
import arm.Beq;
import arm.Cmp;
import cfg.*;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class BrCond implements Instruction {

    Value cond;
    Label ifTrue;
    Label ifFalse;

    public BrCond(Value cond, Label ifTrue, Label ifFalse)
    {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
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
