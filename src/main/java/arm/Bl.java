package arm;

import cfg.InterferenceGraph;
import llvm.value.Register;
import llvm.value.Value;

import java.util.Map;
import java.util.Set;

public class Bl implements Instruction {

    String name;

    public Bl(String name)
    {
        this.name = name;
    }

    public void replaceRegs(Map<String, Register> map, Set<String> spillSet)
    {
    }

    public String getString()
    {
        return ("bl " + name);
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
    }
}
