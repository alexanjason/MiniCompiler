package llvm;

import cfg.*;

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
        return ("br i1 " + cond.getString() + ", label " + ifTrue.getString() + ", label " + ifFalse.getString());
    }
}
