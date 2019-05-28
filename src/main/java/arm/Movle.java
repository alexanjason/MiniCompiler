package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Map;
import java.util.Set;

public class Movle implements Instruction {

    Register r1;
    Value Operand2;

    public Movle(Register r1, Value operand2)
    {
        this.r1 = r1;
        Operand2 = operand2;
    }

    public void replaceRegs(Map<String, Register> map, Set<String> spillSet)
    {
        if (map.containsKey(r1.getString()))
        {
            r1 = map.get(r1.getString());
        }
        else if (spillSet.contains(r1.getString()))
        {
            // TODO can r1 spill?
            System.err.println("Add r1 spilled");
        }
        else
        {
            Register newReg = new Register(new i32(), 5);
            map.put(r1.getString(), newReg);
            r1 = newReg;
        }

        if (Operand2 instanceof Local || Operand2 instanceof Register)
        {
            if (map.containsKey(Operand2.getString()))
            {
                Operand2 = map.get(Operand2.getString());
            }
            else if (spillSet.contains(Operand2.getString())) {
                Register spillReg = new Register(new i32(), 10);
                map.put(Operand2.getString(), spillReg);
                Operand2 = spillReg;
                // TODO add this to mapping?
            } else {
                Register newReg = new Register(new i32(), 5);
                map.put(Operand2.getString(), newReg);
                Operand2 = newReg;
            }
        }
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("movle " + r1.getString() + ", ");
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
        if ((Operand2 instanceof Register) || (Operand2 instanceof Local))
        {
            if (!(killSet.contains(Operand2))) {
                genSet.add(Operand2);
            }
        }

        if (!(killSet.contains(r1))) {
            genSet.add(r1);
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
        if ((Operand2 instanceof Register) || (Operand2 instanceof Local))
        {
            liveSet.add(Operand2);
        }
    }
}
