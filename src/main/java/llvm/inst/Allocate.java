package llvm.inst;


import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Allocate implements Instruction {

    // %<name>= alloca <type>

    private String name;
    private Type type;

    public Allocate(String name, Type type)
    {
        this.name = name;
        this.type = type;
    }

    public String getString()
    {
        return ("%" + name + " = alloca " + type.getString());
    }

    public List<arm.Instruction> getArm()
    {
        // TODO mov or no instruction?
        return new ArrayList<>();
    }
}
