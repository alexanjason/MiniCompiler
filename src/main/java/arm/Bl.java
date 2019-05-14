package arm;

import cfg.Label;

public class Bl implements Instruction {

    String name;

    public Bl(String name)
    {
        this.name = name;
    }

    public String getString()
    {
        return ("bl " + name);
    }
}
