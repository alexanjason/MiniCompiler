package arm;

import cfg.InterferenceGraph;
import llvm.value.Value;

import java.util.Set;

public class Size implements Instruction {

    String name;

    public Size(String name)
    {
        this.name = name;
    }

    public String getString()
    {
        return ".size " + name + ", .-" + name;
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
    }
}
