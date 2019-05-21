package arm;

import cfg.InterferenceGraph;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Set;

public class Add implements Instruction {

    Register r1;
    Value r2;
    Value Operand2;

    public Add(Register r1, Register r2, Value operand2)
    {
        this.r1 = r1;
        this.r2 = r2;
        Operand2 = operand2;
    }

    public Add(Register r1, Local r2, Value operand2)
    {
        this.r1 = r1;
        this.r2 = r2;
        Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + r1.getString() + ", " + r2.getString() + ", ");
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

        if ((Operand2 instanceof Register) || (Operand2 instanceof Local))
        {
            if (!(killSet.contains(Operand2))) {
                genSet.add(Operand2);
            }
        }

        // add target to kill set
        killSet.add(r1);
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
        // remove inst target from live
        liveSet.remove(r1);

        // add an edge from inst target to each element of live
        for (Value v : liveSet)
        {
            graph.addEdge(r1, v);
        }

        // add each source in inst to live
        liveSet.add(r2);

        if ((Operand2 instanceof Register) || (Operand2 instanceof Local))
        {
            liveSet.add(Operand2);
        }
    }
}
