package arm;

import cfg.InterferenceGraph;
import cfg.Label;
import llvm.value.Value;

import java.util.Set;

public class Bge implements Instruction {

    Label label;

    public Bge(Label label)
    {
        this.label = label;
    }

    public String getString()
    {
        return ("bge ." + label.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
    }
}
