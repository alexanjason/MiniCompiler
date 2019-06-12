package llvm.inst;

import arm.*;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.Type;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class Call implements Instruction {

    Value result;
    String name;
    // TODO should be list of parameter values?
    List<Type> paramTypes;
    List<Value> paramVals;

    public Call(Value result, String name, List<Type> paramTypes, List<Value> paramVals)
    {
        this.result = result;
        this.name = name;
        this.paramTypes = paramTypes;
        this.paramVals = paramVals;

        // TODO ????
        result.addDef(this);
        for (Value v : paramVals)
        {
            v.addUse(this);
        }
    }

    public void replace(Value oldV, Value newV)
    {
        for (int i = 0; i < paramVals.size(); i++)
        {
            if (paramVals.get(i) == oldV)
            {
                oldV.getUses().remove(this);
                paramVals.set(i, newV);
            }
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        //System.err.println("sscpinit call " + name);
        map.put(result, new SSCPValue.Bottom());
        workList.add(result);
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        //map.put(result, new SSCPValue.Bottom());
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        for (int i = 0; i < paramVals.size(); i++)
        {
            if (paramVals.get(i) == v)
            {
                v.getUses().remove(this);
                paramVals.set(i, constant);
            }
        }
    }

    public String getString()
    {
        int size = paramVals.size();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(result.getString() + " = call " + result.getType().getString() + " @" + name + "(");
        for (int i = 0; i < size; i++)
        {
            strBuilder.append(paramTypes.get(i).getString());
            strBuilder.append(" ");
            strBuilder.append(paramVals.get(i).getString());

            if (i != size - 1)
            {
                strBuilder.append(", ");
            }
        }
        strBuilder.append(")");
        return strBuilder.toString();
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        for (int i = 0; i < paramVals.size(); i++)
        {
            // TODO does stack based still put args into regs?
            //System.out.println("i: " + i + " paramVal " + paramVals.get(i).getString());
            list.add(new Mov(new Register(paramTypes.get(i), i), paramVals.get(i)));
        }

        Register r = (Register) result;

        // TODO hack
        if (name.equals("read_util"))
        {
            Immediate read_scratch_lower = new Immediate("#:lower16:.read_scratch", new i32());
            Immediate read_scratch_upper = new Immediate("#:upper16:.read_scratch", new i32());
            Immediate read_fmt_lower = new Immediate("#:lower16:.READ_FMT", new i32());
            Immediate read_fmt_upper = new Immediate("#:upper16:.READ_FMT", new i32());

            Register r1 = new Register(new i32(), 1);
            Register r0 = new Register(new i32(), 0);

            list.add(new Movw(r1, read_scratch_lower));
            list.add(new Movt(r1, read_scratch_upper));

            list.add(new Movw(r0, read_fmt_lower));
            list.add(new Movt(r0, read_fmt_upper));

            list.add(new Bl("scanf"));


            list.add(new Movw(r, read_scratch_lower));
            list.add(new Movt(r, read_scratch_upper));

            list.add(new Ldr(r, r));

        }
        else
        {
            list.add(new Bl(name));
            list.add(new Mov(r, new Register(result.getType(), 0)));
        }

        return list;
    }
}
