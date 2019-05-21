package arm;

import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Set;

public class Str implements Instruction {

    Value r1;
    Register r2;

    public Str(Register r1, Register r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public Str(Local r1, Register r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public String getString()
    {
        return ("str " + r1.getString() + ", [" + r2.getString() + "]");
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
        // add each source not in kill set to gen set
        if (!(killSet.contains(r2)))
        {
            genSet.add(r2);
        }

        // add target to kill set
        killSet.add(r1);
    }
}
