package arm;

import cfg.Label;

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
}
