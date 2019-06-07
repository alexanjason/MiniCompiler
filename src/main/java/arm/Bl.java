package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Map;
import java.util.Set;

public class Bl implements Instruction {

    String name;

    public Bl(String name)
    {
        this.name = name;
    }

    public void replaceRegs(Map<String, Register> map, Set<String> spillSet)
    {
    }

    public String getString()
    {
        return ("bl " + name);
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
        Register r0 = new Register(new i32(), 0);
        Register r1 = new Register(new i32(), 0);
        Register r2 = new Register(new i32(), 0);
        Register r3 = new Register(new i32(), 0);

        // remove inst target from live
        liveSet.remove(r0);

        // add an edge from inst target to each element of live
        for (Value v : liveSet)
        {
            graph.addEdge(r0, v);
        }

        // remove inst target from live
        liveSet.remove(r1);

        // add an edge from inst target to each element of live
        for (Value v : liveSet)
        {
            graph.addEdge(r1, v);
        }

        // remove inst target from live
        liveSet.remove(r2);

        // add an edge from inst target to each element of live
        for (Value v : liveSet)
        {
            graph.addEdge(r2, v);
        }

        // remove inst target from live
        liveSet.remove(r3);

        // add an edge from inst target to each element of live
        for (Value v : liveSet)
        {
            graph.addEdge(r3, v);
        }

    }
}
