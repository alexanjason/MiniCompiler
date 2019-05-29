package llvm.inst;

import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class Getelementptr implements Instruction {

    Value result;
    Value base;
    int index;

    public Getelementptr(Value result, Value base, int index)
    {
        this.result = result;
        this.base = base;
        this.index = index;

        result.addDef(this);
        base.addUse(this);
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit getelementptr");
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval getelementptr");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (base == v)
        {
            base = constant;
        }
    }

    public String getString()
    {
        String typeStr = base.getType().getString();
        String typeStrNotPtr = typeStr.substring(0, typeStr.length() - 1);
        return (result.getString() + " = getelementptr " + typeStrNotPtr + ", " + typeStr + " " +
        base.getString() + ", i1 0, i32 " + index);
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();

        //System.out.println("getelementptr -> result: " + result.getString() + " base: " + base.getString());

        Register resultReg = (Register) result;
        //Register baseReg = (Register) base;

        if (base instanceof Register)
        {
            list.add(new arm.Add(resultReg, (Register)base, new Immediate(Integer.toString(index * 4), new i32())));
        }
        else if (base instanceof Local)
        {
            list.add(new arm.Add(resultReg, (Local)base, new Immediate(Integer.toString(index * 4), new i32())));
        }
        else
        {
            System.err.println("getelementptr base error");
        }

        return list;
    }
}
