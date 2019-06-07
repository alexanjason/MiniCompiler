package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Map;
import java.util.Set;

public class Sdiv implements Instruction {

    Register r1;
    Value r2; // TODO sus
    Value r3; // TODO sus

    public Sdiv(Register r1, Register r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Sdiv(Register r1, Local r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Sdiv(Register r1, Local r2, Local r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Sdiv(Register r1, Register r2, Local r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
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
            System.err.println("sdiv r1 spilled " + r1.getString());
        }
        else
        {
            System.err.println("sdiv r1 NOT IN GRAPH: " + r1.getString());
        }

        if (map.containsKey(r2.getString()))
        {
            r2 = map.get(r2.getString());
        }
        else if (spillSet.contains(r2.getString()))
        {
            Register spillReg = new Register(new i32(), 9);
            map.put(r2.getString(), spillReg);
            r2 = spillReg;
            // TODO add this to mapping?
        }
        else
        {
            System.err.println("sdiv r1 NOT IN GRAPH: " + r2.getString());
        }

        if (map.containsKey(r3.getString()))
        {
            r3 = map.get(r3.getString());
        }
        else if (spillSet.contains(r3.getString())) {
            Register spillReg = new Register(new i32(), 10);
            map.put(r3.getString(), spillReg);
            r3 = spillReg;
            // TODO add this to mapping?
        } else {
            System.err.println("sdiv r1 NOT IN GRAPH: " + r3.getString());
        }

    }

    public String getString()
    {
        return ("sdiv " + r1.getString() + ", " + r2.getString() + ", " + r3.getString());
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
        liveSet.add(r3);
    }
}