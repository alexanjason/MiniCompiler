package llvm.inst;

import arm.Bl;
import arm.Mov;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.Type;
import llvm.type.Void;
import llvm.value.*;

import java.util.*;

public class CallVoid implements Instruction {

    String name;
    List<Type> paramTypes;
    List<Value> paramVals;

    public CallVoid(String name, List<Type> paramTypes, List<Value> paramVals)
    {
        this.name = name;
        this.paramTypes = paramTypes;
        this.paramVals = paramVals;

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
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
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
        Type voidType = new Void();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("call " + voidType.getString() + " @" + name + "(");
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
            list.add(new Mov(new Register(paramTypes.get(i), i), paramVals.get(i)));
        }
        list.add(new Bl(name));

        return list;
    }
}
