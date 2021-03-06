package arm;

import cfg.InterferenceGraph;
import cfg.Label;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class B implements Instruction {

    Label label;

    public B(Label label)
    {
        this.label = label;
    }

    public void replaceRegs(ListIterator<Instruction> instList, Map<String, Register> map, Map<String, Integer> spillMap)
    {
    }

    public String getString()
    {
        return ("b ." + label.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }

    public void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph)
    {
    }
}
