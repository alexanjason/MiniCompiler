package arm;

import cfg.Label;

public class Bge implements Instruction {

    Label label;

    public Bge(Label label)
    {
        this.label = label;
    }

    public String getString()
    {
        return ("bge ." + label.getString());
    }
}
