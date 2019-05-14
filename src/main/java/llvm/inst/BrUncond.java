package llvm.inst;

import arm.B;
import cfg.Label;

import java.util.ArrayList;
import java.util.List;

public class BrUncond implements Instruction {

    Label dest;

    public BrUncond(Label dest)
    {
        this.dest = dest;
    }

    public String getString()
    {
        return ("br label %" + dest.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new B(dest));
        return list;
    }
}
