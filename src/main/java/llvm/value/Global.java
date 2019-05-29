package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Global implements Value {

    String id;
    Type type;
    Instruction def;
    List<Instruction> uses;
    boolean marked;

    public Global(String id, Type type)
    {
        this.id = id;
        this.type = type;
        this.uses = new ArrayList<>();
        this.marked = false;
    }

    public boolean isMarked()
    {
        return this.marked;
    }

    public void checkUseless()
    {
        if (this.uses.size() == 0)
        {
            marked = true;
        }
    }

    public void addDef(Instruction def)
    {
        this.def = def;
        //System.err.println("addDef global");
    }

    public void addUse(Instruction use)
    {
        this.uses.add(use);
        //System.err.println("addUse global");
    }

    public Instruction getDef()
    {
        return def;
    }

    public List<Instruction> getUses()
    {
        return uses;
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
