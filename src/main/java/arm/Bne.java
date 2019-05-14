package arm;

import cfg.Label;

public class Bne implements Instruction {

    Label label;

    public Bne(Label label)
    {
        this.label = label;
    }

    public String getString()
    {
        return ("bne ." + label.getString());
    }

}
