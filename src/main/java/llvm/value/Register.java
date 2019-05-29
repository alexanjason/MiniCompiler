package llvm.value;

import ast.exp.IntegerExpression;
import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class Register implements Value {

    Type type;
    static private int increment = 0;
    private int id;
    Instruction def;            //TODO
    List<Instruction> uses;     //TODO

    boolean real = false;       // TODO hacky

    public Register(Type type)
    {
        this.type = type;
        this.id = increment++;
        //this.def = def;
        this.uses = new ArrayList<>();
    }

    public Register(Type type, int num)
    {
        this.type = type;
        this.id = num;
        this.uses = new ArrayList<>();
        real = true;
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

    public boolean isReal()
    {
        return real;
    }

    public String getId()
    {
        return Integer.toString(id);
    }

    public Type getType()
    {
        return this.type;
    }

    public String getString()
    {
        if (real)
        {
            return ("r" + id);
        }
        else
        {
            return ("%v" + id);
        }
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
        return this.id;
    }
}
