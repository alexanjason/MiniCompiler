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
    public Set<String> spillSet;

    public InterferenceGraph()
    {
        map = new HashMap<>();
        stack = new Stack<>();
        registerSet = new HashSet<>();

        // available registers
        for (int i = 4; i <= 8; i++)
        {
            registerSet.add(new Register(new i32(), i));
        }
        // r0-r3 function calls r9-r10 spill regs

        regMappings = new HashMap<>();
        spillSet = new HashSet<>();

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

    private ValueVertex addVertex(Value v)
    {
        ValueVertex vertex = new ValueVertex(v);
        //System.out.println("adding to graph " + v.getString());
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
            System.out.println("addEdge: from " + s1.getString() + " to " + s2.getString());

            ValueVertex v1 = addVertex(s1);
            ValueVertex v2 = addVertex(s2);

            v1.addEdge(v2);
            v2.addEdge(v1);

            //System.out.println("add edge from " + v1.v.getString() + " to " + v2.v.getString());

            //v1.adjList.putIfAbsent(s2.getString(), s2);
            //v2.adjList.putIfAbsent(s1.getString(), s1);
        }
    }

    public void regAlloc()
    {
        printGraph();
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
        //for (String s : keys)
        //for (int i = 0; i < map.keySet().size(); i++)
        while (keys.hasNext())
        {
            String s = keys.next();
            //map.remove(s);
            //System.out.println("removeNode " + s);
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
            if (!(vStr.equals("r0") || vStr.equals("r1") || vStr.equals("r2") || vStr.equals("r3")))
            {
                if (colorV.color(registerSet))
                {
                    regMappings.put(colorV.v.getString(), colorV.color);
                }
                else
                {
                    spillSet.add(colorV.v.getString());
                }
            }
        }

        // "real" registers
        // TODO this is unnecessary?
        /*
        for (int i = 0; i <= 3; i++)
        {
            ValueVertex colorV = stack.pop();
            colorV.color(new Register(new i32(), i));
        }
        */

    }
}
