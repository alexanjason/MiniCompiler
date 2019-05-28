package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Map;
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

    public void replaceRegs(Map<String, Register> map, Set<String> spillSet)
    {
        if (map.containsKey(r1.getString()))
        {
            r1 = map.get(r1.getString());
        }
        else if (spillSet.contains(r1.getString()))
        {
            // TODO can r1 spill?
            System.err.println("str r1 spilled " + r1.getString());
        }
        else
        {
            Register newReg = new Register(new i32(), 5);
            map.put(r1.getString(), newReg);
            r1 = newReg;
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
            Register newReg = new Register(new i32(), 5);
            // TODO
            map.put(r2.getString(), newReg);
            r2 = newReg;
        }
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
    }
}
