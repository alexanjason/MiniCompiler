package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Local implements Value {

    String id;
    Type type;
    boolean isParam;
    Instruction def;            //TODO
    List<Instruction> uses;     //TODO

    public Local(String id, Type type)
    {
        this.id = id;
        this.type = type;
        this.isParam = false;
        this.uses = new ArrayList<>();
    }

    // TODO hacky
    public Local(String id, Type type, boolean parameter)
    {
        this.id = id;
        this.type = type;
        this.isParam = true;
        this.uses = new ArrayList<>();
    }

    public boolean isParam()
    {
        return isParam;
    }

    public void addDef(Instruction def)
    {
        this.def = def;
    }

    public void addUse(Instruction use)
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

    public String getString()
    {
        return ("%" + id);
    }

    public String getId()
    {
        return id;
    }

    public Type getType()
    {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Value)) {
            return false;
        }

        Value c = (Value) o;

        return this.getString().equals(c.getString());
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }
}
