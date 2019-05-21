package arm;

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
}
