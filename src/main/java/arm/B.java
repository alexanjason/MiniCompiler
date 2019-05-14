package arm;

import cfg.Label;

public class B implements Instruction {

    Label label;

    public B(Label label)
    {
        this.label = label;
    }

    public String getString()
    {
        return ("b ." + label.getString());
    }
}
