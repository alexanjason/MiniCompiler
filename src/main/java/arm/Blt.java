package arm;

import cfg.Label;
import llvm.value.Value;

import java.util.Set;

public class Blt implements Instruction {

    Label label;

    public Blt(Label label)
    {
        this.label = label;
    }

    public String getString()
    {
        return ("blt ." + label.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }
}
