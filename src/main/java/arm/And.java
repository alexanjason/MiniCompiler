package arm;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Set;

public class And implements Instruction {

    Register r1;
    Register r2;
    Value Operand2;

    public And(Register r1, Register r2, Value operand2)
    {
        this.r1 = r1;
        this.r2 = r2;
        Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("and " + r1.getString() + ", " + r2.getString() + ", ");
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
        if (!(killSet.contains(r2)))
        {
            genSet.add(r2);
        }

        if (!(killSet.contains(Operand2)))
        {
            genSet.add(Operand2);
        }

        // add target to kill set
        killSet.add(r1);
    }
}
