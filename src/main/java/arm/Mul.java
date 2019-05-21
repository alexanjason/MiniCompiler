package arm;

import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Set;

public class Mul implements Instruction {

    Register r1;
    Value r2;
    Value r3;

    public Mul(Register r1, Register r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Mul(Register r1, Local r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2; // TODO sus
        this.r3 = r3;
    }

    public Mul(Register r1, Local r2, Local r3)
    {
        this.r1 = r1;
        this.r2 = r2; // TODO sus
        this.r3 = r3; // TODO sus
    }

    public String getString()
    {
        return ("mul " + r1.getString() + ", " + r2.getString() + ", " + r3.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
        // add each source not in kill set to gen set
        if (!(killSet.contains(r2)))
        {
            genSet.add(r2);
        }

        if (!(killSet.contains(r3)))
        {
            genSet.add(r3);
        }

        // add target to kill set
        killSet.add(r1);
    }
}
