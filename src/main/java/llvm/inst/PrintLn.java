package llvm.inst;

import arm.Bl;
import arm.Mov;
import arm.Movt;
import arm.Movw;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;
import llvm.type.Type;

import java.util.*;

public class PrintLn implements Instruction {

    Value val;
    Type type;

    public PrintLn(Value val, Type type)
    {
        this.val = val;
        this.type = type;

        val.addUse(this);
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit println");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (val == v)
        {
            val.getUses().remove(this);
            val = constant;
        }
    }

    public void replace(Value oldV, Value newV)
    {
        if (val == oldV)
        {
            val.getUses().remove(this);
            val = newV;
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
    }

    public String getString()
    {
        return ("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), " +
                type.getString() + " " + val.getString() + ")");
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(new Register(val.getType(), 1), val));
        Immediate println_fmt_lower = new Immediate("#:lower16:.PRINTLN_FMT", new i32());
        Immediate println_fmt_upper = new Immediate("#:upper16:.PRINTLN_FMT", new i32());

        Register r0 = new Register(new i32(), 0);

        list.add(new Movw(r0, println_fmt_lower));
        list.add(new Movt(r0, println_fmt_upper));

        list.add(new Bl("printf"));
        return list;
    }

}
