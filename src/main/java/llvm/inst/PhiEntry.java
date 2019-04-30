package llvm.inst;

import cfg.Label;
import llvm.value.Value;

public class PhiEntry {

    Value value;
    Label label;

    public PhiEntry(Value value, Label label)
    {
        this.value = value;
        this.label = label;
    }
}
