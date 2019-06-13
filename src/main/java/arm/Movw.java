package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.*;

import java.util.ListIterator;
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

    public void replaceRegs(ListIterator<Instruction> instList, Map<String, Register> map, Map<String, Integer> spillMap)
    {

        Register r9 = new Register(new i32(), 9);

        if (map.containsKey(r1.getString()))
        {
            r1 = map.get(r1.getString());
        }
        else if (spillMap.containsKey(r1.getString()))
        {
            int offset = spillMap.get(r1.getString());
            instList.add(new Str(r9, new StackLocation(offset*4)));
            r1 = r9;
        }
        else
        {
            System.err.println("r1 NOT IN GRAPH: " + r1.getString());
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

        //add vertex for r1
        graph.addVertex(r1);

        // add an edge from inst target to each element of live
        for (Value v : liveSet)
        {
            graph.addEdge(r1, v);
        }

        liveSet.add(r1);
    }
}
