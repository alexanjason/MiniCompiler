package cfg;

import llvm.value.Register;
import llvm.value.Value;

import java.util.*;

public class ValueVertex {

    Value v;
    Map<String, ValueVertex> adjList;

    Register color;

    ValueVertex(Value v)
    {
        this.v = v;
        this.adjList = new HashMap<>();
    }

    public void removeEdge(String s)
    {
        adjList.remove(s);
    }

    public void addEdge(ValueVertex v)
    {
        adjList.put(v.v.getString(), v);
    }

    public void printEdges()
    {
        for (String s : adjList.keySet())
        {
            System.out.print(s + " ");
        }
    }

    public boolean color(Set<Register> availableRegs)
    {
        for (Register r : availableRegs)
        {
            boolean canColor = true;
            // if no neighbors have that color, can color with that color
            for (ValueVertex v : this.adjList.values())
            {
                if (v.color == r)
                {
                    canColor = false;
                    break;
                }
            }
            if (canColor)
            {
                this.color = r;
                return true;
            }
        }

        System.err.println("***SPILLED: " + this.v.getString());
        return false;
    }
}
