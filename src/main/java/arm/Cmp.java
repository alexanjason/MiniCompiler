package arm;

import cfg.InterferenceGraph;
import llvm.type.i32;
import llvm.value.*;

import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Cmp implements Instruction {

    Value r1;
    Value Operand2;

    public Cmp(Register r1, Value operand2)
    {
        this.r1 = r1;
        Operand2 = operand2;
    }

    public Cmp(Local r1, Value operand2)
    {
        this.r1 = r1;
        Operand2 = operand2;
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
            }
            else {
                System.err.println("cmp operand2 NOT IN GRAPH: " + Operand2.getString());
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
            System.err.println("cmp r1 NOT IN GRAPH: " + r1.getString());
        }
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("cmp " + r1.getString() + ", ");
        if (Operand2 instanceof Immediate)
        {
            sb.append("#");
        }
        if (Operand2.getString().equals("null"))
        {
            sb.append("0");
        }
        else
        {
            sb.append(Operand2.getString());
        }
        //sb.append(Operand2.getString());
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
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
        // add each source in inst to live
        if ((Operand2 instanceof Register) || (Operand2 instanceof Local))
        {
            liveSet.add(Operand2);
        }

        liveSet.add(r1);
    }
}
