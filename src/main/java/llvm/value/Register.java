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
    }

    public String getId()
    {
        return Integer.toString(id);
    }

    public void addDef(Instruction def)
    {
        this.def = def;
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
