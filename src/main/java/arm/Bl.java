package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Bl implements Instruction {

    String name;

    public Bl(String name)
    {
        this.name = name;
    }

    public void replaceRegs(ListIterator<Instruction> instList, Map<String, Register> map, Map<String, Integer> spillMap)
    {
    }

    public String getString()
    {
        return ("bl " + name);
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
        Register r0 = new Register(new i32(), 0);
        Register r1 = new Register(new i32(), 1);
        Register r2 = new Register(new i32(), 2);
        Register r3 = new Register(new i32(), 3);

        // add each source not in kill set to gen set
        if (!(killSet.contains(r0)))
        {
            genSet.add(r0);
        }
        if (!(killSet.contains(r1)))
        {
            genSet.add(r1);
        }
        if (!(killSet.contains(r2)))
        {
            genSet.add(r2);
        }
        if (!(killSet.contains(r3)))
        {
            genSet.add(r3);
        }

        // add target to kill set
        killSet.add(r0);
        killSet.add(r1);
        killSet.add(r2);
        killSet.add(r3);
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {

        Register r0 = new Register(new i32(), 0);
        Register r1 = new Register(new i32(), 1);
        Register r2 = new Register(new i32(), 2);
        Register r3 = new Register(new i32(), 3);

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
