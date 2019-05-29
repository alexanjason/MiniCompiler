package llvm.inst;

import arm.Bl;
import arm.Mov;
import cfg.SSCPValue;
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

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit println");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (val == v)
        {
            val = constant;
        }
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval println");
    }

    public String getString()
    {
        return ("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), " +
                type.getString() + " " + val.getString() + ")");
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(new Register(val.getType(), 0), val));
        list.add(new Bl("println"));
        return list;
    }

}
