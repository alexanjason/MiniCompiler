package arm;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Set;

public class Movw implements Instruction {

    Register r1;
    Immediate imm16;

    public Movw(Register r1, Immediate imm16)
    {
        this.r1 = r1;
        this.imm16 = imm16;
    }

    public String getString()
    {
        return ("movw " + r1.getString() + ", " + imm16.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
        // add target to kill set
        killSet.add(r1);
    }
}
