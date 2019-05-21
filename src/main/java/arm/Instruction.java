package arm;

import llvm.value.Value;

import java.io.PrintStream;
import java.util.Set;

public interface Instruction {

    String getString();

    default void print(PrintStream stream)
    {
        stream.println("\t" + this.getString());
    }

    void addToGenAndKill(Set<Value> genSet, Set<Value> killSet);
}
