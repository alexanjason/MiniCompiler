package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Register implements Value {

    Type type;
    static private int increment = 0;
    private int id;
    Instruction def;
    List<Instruction> uses;

    public Register(Type type, Instruction def)
    {
        this.type = type;
        this.id = increment++;
        this.def = def;
        this.uses = new ArrayList<>();
    }

    public Type getType()
    {
        return this.type;
    }

    public String getString()
    {
        return ("%v" + id);
    }
}
