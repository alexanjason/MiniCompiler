package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Immediate implements Value {

    String val;
    Type type;

    public Immediate(String val, Type type)
    {
        this.val = val;
        this.type = type;
    }

    public void addDef(Instruction def)
    {
        System.err.println("addDef immediate");
    }

    public void addUse(Instruction use)
    {
        //System.err.println("addUse immediate");
    }

    public Instruction getDef()
    {
        return null;
    }

    public List<Instruction> getUses()
    {
        return new ArrayList<>();
    }

    public String getId()
    {
        return val;
    }

    public String getString()
    {
        return val;
    }

    public Type getType()
    {
        return this.type;
    }
}
