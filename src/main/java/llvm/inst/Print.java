package llvm.inst;

import arm.Bl;
import arm.Mov;
import cfg.SSCPValue;
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
            val = constant;
        }
    }

    public String getString()
    {
        return ("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.print, i32 0, i32 0), " +
                val.getType().getString() + " " + val.getString() + ")");
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(new Register(val.getType(), 0), val));
        list.add(new Bl("print"));
        return list;
    }

}