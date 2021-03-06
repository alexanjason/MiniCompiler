package cfg;

import llvm.type.i32;
import llvm.value.Register;
import llvm.value.Value;

import java.util.*;

public class InterferenceGraph {

    private Map<String, ValueVertex> map;
    private Stack<ValueVertex> stack;
    private Set<Register> registerSet;

    public Map<String, Register> regMappings;
    public Map<String, Integer> spillMap;

    int spillCount;

    public InterferenceGraph()
    {
        map = new HashMap<>();
        stack = new Stack<>();
        registerSet = new HashSet<>();

        // available registers
        for (int i = 4; i <= 10; i++)
        {
            registerSet.add(new Register(new i32(), i));
        }
        // r0-r3 function calls r9-r10 spill regs

        regMappings = new HashMap<>();
        spillMap = new HashMap<>();

        // TODO hackiest of hacks
        for (int i = 0; i <=3; i++)
        {
            regMappings.put("r" + i, new Register(new i32(), i));
        }
        for (int i = 11; i <=13; i++)
        {
            regMappings.put("r" + i, new Register(new i32(), i));
        }
    }

    public void printGraph()
    {
        System.out.println("Print Graph " + map.keySet().size());

        for (String s : map.keySet())
        {
            ValueVertex v = map.get(s);
            System.out.print(s + " : ");
            v.printEdges();
            System.out.println();
        }
    }

    public void printMap()
    {
        System.out.println("Print Map");

        for (String s : regMappings.keySet())
        {
            System.out.println(s + " -> " + regMappings.get(s).getString());
        }
    }

    public ValueVertex addVertex(Value v)
    {
        ValueVertex vertex = new ValueVertex(v);
        ValueVertex newVert = map.putIfAbsent(v.getString(), vertex);
        if (newVert == null)
        {
            return vertex;
        }
        else
        {
            return newVert;
        }
    }

    public void addEdge(Value s1, Value s2)
    {
        // TODO sloppy
        if (!((s1 instanceof Register && ((Register)s1).isReal()) || (s2 instanceof Register && ((Register)s2).isReal()))) {

            ValueVertex v1 = addVertex(s1);
            ValueVertex v2 = addVertex(s2);

            v1.addEdge(v2);
            v2.addEdge(v1);
        }
    }

    public void regAlloc()
    {
        //printGraph();
        deconstruct();
        color();
    }

    private void deconstruct()
    {
        // remove "real" registers
        for (int i = 3; i >= 0; i--)
        {
            String reg = "r" + i;
            removeNode(reg);
        }

        // remove virtual registers
        Iterator<String> keys = map.keySet().iterator();
        while (keys.hasNext())
        {
            String s = keys.next();
            removeNode(s);
            keys.remove();
        }
    }

    private void removeNode(String reg)
    {
        ValueVertex remove = map.get(reg);
        if (remove != null)
        {
            stack.push(remove);

            for (String s : remove.adjList.keySet()) {
                map.get(s).removeEdge(reg);
            }
        }
    }

    private void color()
    {
        while (stack.size() > 0)
        {
            ValueVertex colorV = stack.pop();
            String vStr = colorV.v.getString();
            // TODO dumb
            if (!(vStr.equals("r0") || vStr.equals("r1") || vStr.equals("r2") || vStr.equals("r3") || vStr.equals("r11") || vStr.equals("r13") || vStr.equals("r12")))
            {
                if (colorV.color(registerSet))
                {
                    regMappings.put(colorV.v.getString(), colorV.color);
                }
                else
                {
                    //System.err.println("*SPILLED");
                    spillMap.put(colorV.v.getString(), spillCount+9);
                    spillCount++;
                }
            }
        }
    }
}
