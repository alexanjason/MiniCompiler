package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.*;

import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Moveq implements Instruction {

    Register r1;
    Value Operand2;

    public Moveq(Register r1, Value operand2)
    {
        this.r1 = r1;
        Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("moveq " + r1.getString() + ", ");
        if (Operand2 instanceof Immediate)
        {
            sb.append("#");
        }
        sb.append(Operand2.getString());
        return sb.toString();
    }

    public void replaceRegs(ListIterator<Instruction> instList, Map<String, Register> map, Map<String, Integer> spillMap)
    {

        Register r9 = new Register(new i32(), 9);
        Register r10 = new Register(new i32(), 10);

        if (Operand2 instanceof Local || Operand2 instanceof Register)
        {
            if (map.containsKey(Operand2.getString()))
            {
                Operand2 = map.get(Operand2.getString());
            }
            else if (spillMap.containsKey(Operand2.getString())) {
                instList.previous();
                int offset = spillMap.get(Operand2.getString());
                instList.add(new Ldr(r10, new StackLocation(offset*4)));
                instList.next();
                Operand2 = r10;
            } else {
                System.err.println("Operand2 NOT IN GRAPH: " + Operand2.getString());
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
            //instList.next();
            r1 = r9;
        }
        else
        {
            System.err.println("r1 NOT IN GRAPH: " + r1.getString());
        }
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

        // add each source in inst to live
        if ((Operand2 instanceof Register) || (Operand2 instanceof Local))
        {
            liveSet.add(Operand2);
        }
    }
}
