package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Global implements Value {

    String id;
    Type type;

    public Global(String id, Type type)
    {
        this.id = id;
        this.type = type;
    }

    public void addDef(Instruction def)
    {
        System.err.println("addDef global");
    }

    public void addUse(Instruction use)
    {
        System.err.println("addUse global");
    }

    public Instruction getDef()
    {
        return null;
    }

    public List<Instruction> getUses()
    {
        return new ArrayList<>();
    }

    public String getString()
    {
        return ("@" + id);
    }

    public String getId()
    {
        return id;
    }

    public Type getType()
    {
        return this.type;
    }
}
