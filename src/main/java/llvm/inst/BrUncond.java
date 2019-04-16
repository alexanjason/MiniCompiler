package llvm.inst;

import cfg.Label;

public class BrUncond implements Instruction {

    Label dest;

    public BrUncond(Label dest)
    {
        this.dest = dest;
    }

    public String getString()
    {
        return ("br label " + dest.getString());
    }
}
