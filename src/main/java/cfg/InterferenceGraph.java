package cfg;

import llvm.value.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterferenceGraph {

    private Map<ValueVertex, List<ValueVertex>> adjList;

    public InterferenceGraph()
    {
        adjList = new HashMap<>();
    }

    public ValueVertex addVertex(Value v)
    {
        ValueVertex vertex = new ValueVertex(v);
        // TODO Value equality?
        adjList.putIfAbsent(vertex, new ArrayList<>());
        return vertex;
    }

    public void addEdge(Value s1, Value s2)
    {
        //ValueVertex v1 = new ValueVertex(s1);
        //ValueVertex v2 = new ValueVertex(s2);
        ValueVertex v1 = addVertex(s1);
        ValueVertex v2 = addVertex(s2);

        adjList.get(v1).add(v2);
        adjList.get(v2).add(v1);
    }

}
