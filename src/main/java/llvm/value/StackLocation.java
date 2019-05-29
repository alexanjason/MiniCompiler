package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class StackLocation implements Value {

    static int increment = 0;
    private int id;
    Type type;
    Instruction def;            //TODO
    List<Instruction> uses;     //TODO

    public StackLocation(Type type)
    {
        this.id = increment;
        increment++;
        this.type = type;
        this.uses = new ArrayList<>();
    }

    public void addDef(llvm.inst.Instruction def)
    {
        this.def = def;
    }

    public void addUse(llvm.inst.Instruction use)
    {
        this.uses.add(use);
    }

    public Instruction getDef()
    {
        return def;
    }

    public List<Instruction> getUses()
    {
        return uses;
    }

    public String getId()
    {
        return Integer.toString(id);
    }

    public String getString()
    {
        return ("%u" + this.id);
    }

    public Type getType()
    {
        return this.type;
    }
}
