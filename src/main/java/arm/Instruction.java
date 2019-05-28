package arm;

import cfg.InterferenceGraph;
import llvm.value.Register;
import llvm.value.Value;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

public interface Instruction {

    String getString();

    default void print(PrintStream stream)
    {
        stream.println("\t" + this.getString());
    }

    void addToGenAndKill(Set<Value> genSet, Set<Value> killSet);

    void addToInterferenceGraph(Set<Value> liveSet, InterferenceGraph graph);

    void replaceRegs(Map<String, Register> map, Set<String> spillSet);

}
