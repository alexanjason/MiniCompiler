package llvm.inst;

import arm.Bl;
import arm.Mov;
import arm.Movt;
import arm.Movw;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class Print implements Instruction {

    Value val;
    //Type type;

    public Print(Value val)
    {
        this.val = val;

        val.addUse(this);
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit print");
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        //System.err.println("sscpEval print");
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

    public String getString()
    {
        return ("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.print, i32 0, i32 0), " +
                val.getType().getString() + " " + val.getString() + ")");
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(new Register(val.getType(), 1), val));
        Immediate print_fmt_lower = new Immediate("#:lower16:.PRINT_FMT", new i32());
        Immediate print_fmt_upper = new Immediate("#:upper16:.PRINT_FMT", new i32());

        Register r0 = new Register(new i32(), 0);

        list.add(new Movw(r0, print_fmt_lower));
        list.add(new Movt(r0, print_fmt_upper));

        list.add(new Bl("printf"));
        return list;
    }

}