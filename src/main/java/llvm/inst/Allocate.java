package llvm.inst;


import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.Type;
import llvm.value.*;

import java.util.*;

public class Allocate implements Instruction {

    // %<name>= alloca <type>

    private String name;
    private Type type;

    public Allocate(String name, Type type)
    {
        this.name = name;
        this.type = type;

        // TODO def or use?
    }

    public void replace(Value oldV, Value newV)
    {
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval allocate");
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscp init allocate");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        System.err.println("sscpReplace allocate");
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
