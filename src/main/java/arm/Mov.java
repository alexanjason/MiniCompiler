package arm;

import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Set;

public class Mov implements Instruction {

    Value r1;
    Value Operand2;

    public Mov(Register r1, Value operand2)
    {
        this.r1 = r1;
        this.Operand2 = operand2;
    }

    public Mov(Local r1, Value operand2)
    {
        this.r1 = r1;
        this.Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("mov " + r1.getString() + ", ");
        if (Operand2 instanceof Immediate)
        {
            sb.append("#");
        }
        sb.append(Operand2.getString());
        return sb.toString();
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
        // add each source not in kill set to gen set
        if (!(killSet.contains(Operand2)))
        {
            genSet.add(Operand2);
        }

        // add target to kill set
        killSet.add(r1);
    }
}
