package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.StackLocation;
import llvm.value.Value;

import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Sdiv implements Instruction {

    Register r1;
    Value r2;
    Value r3;

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

    public void replaceRegs(ListIterator<Instruction> instList, Map<String, Register> map, Map<String, Integer> spillMap)
    {

        Register r9 = new Register(new i32(), 9);
        Register r10 = new Register(new i32(), 10);

        if (map.containsKey(r2.getString()))
        {
            r2 = map.get(r2.getString());
        }
        else if (spillMap.containsKey(r2.getString()))
        {
            instList.previous();
            int offset = spillMap.get(r2.getString());
            instList.add(new Ldr(r9, new StackLocation(offset*4)));
            instList.next();
            r2 = r9;
        }
        else
        {
            System.err.println("r2 NOT IN GRAPH: " + r2.getString());
        }

        if (r3 instanceof Local || r3 instanceof Register)
        {
            if (map.containsKey(r3.getString()))
            {
                r3 = map.get(r3.getString());
            }
            else if (spillMap.containsKey(r3.getString())) {
                instList.previous();
                int offset = spillMap.get(r3.getString());
                instList.add(new Ldr(r10, new StackLocation(offset*4)));
                instList.next();
                r3 = r10;
            } else {
                System.err.println("Operand2 NOT IN GRAPH: " + r3.getString());
            }
        }

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

        //add vertex for r1
        graph.addVertex(r1);

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