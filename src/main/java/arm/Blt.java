package arm;

import cfg.Label;

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
}
