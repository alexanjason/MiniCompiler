package cfg;

import llvm.value.Register;
import llvm.value.Value;

import java.util.*;

public class ValueVertex {

    Value v;
    Map<String, ValueVertex> adjList;

    Register color;
    //boolean spill;

    ValueVertex(Value v)
    {
        this.v = v;
        this.adjList = new HashMap<>();
        //this.spill = false;
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
                    //System.out.println("can't color: " + this.v.getString() + " to " + r.getString());
                    canColor = false;
                    break;
                }
            }
            if (canColor)
            {
                //System.out.println("coloring: " + this.v.getString() + " to " + r.getString());
                this.color = r;
                return true;
            }
        }

        System.err.println("***SPILLED: " + this.v.getString());
        //this.spill = true;
        return false;
    }

    /*
    public void color(Register reg)
    {
        this.color = reg;
    }
    */

}
