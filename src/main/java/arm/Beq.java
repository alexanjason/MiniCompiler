package arm;

import cfg.Label;
import llvm.value.Value;

import java.util.Set;

public class Beq implements Instruction {

    Label label;

    public Beq(Label label)
    {
        this.label = label;
    }

    public String getString()
    {
        return ("beq ." + label.getString());
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }
}
