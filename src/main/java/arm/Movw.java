package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Map;
import java.util.Set;

public class Movw implements Instruction {

    Register r1;
    Immediate imm16;

    public Movw(Register r1, Immediate imm16)
    {
        this.r1 = r1;
        this.imm16 = imm16;
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
    }

    public String getString()
    {
        return ("movw " + r1.getString() + ", " + imm16.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
        // add source
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

        liveSet.add(r1);
    }
}
