package llvm.inst;

import arm.Mov;
import cfg.SSCPValue;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.*;

public class Zext implements Instruction {

    Value outValue;
    Value inValue;

    public Zext(Value inValue, Value outValue)
    {
        this.outValue = outValue;
        this.inValue = inValue;

        outValue.addDef(this);
        inValue.addUse(this);
    }

    public String getString()
    {
        return (outValue.getString() + " = zext " + inValue.getType().getString() + " " + inValue.getString() + " to " + outValue.getType().getString());
    }

    public boolean checkRemove(ListIterator list)
    {
        if (outValue.isMarked())
        {
            list.remove();
            return true;
        }
        return false;
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (inValue == v)
        {
            inValue = constant;
        }
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        if (!(map.get(outValue) instanceof SSCPValue.Bottom))
        {
            SSCPValue oldResult = map.get(outValue);
            SSCPValue newResult;

            SSCPValue sscpIn;

            if (inValue instanceof Immediate)
            {
                sscpIn = new SSCPValue.Constant(Boolean.parseBoolean(inValue.getId()));
            }
            else
            {
                sscpIn = map.get(inValue);
            }


            if (sscpIn instanceof SSCPValue.Bottom)
            {
                newResult = new SSCPValue.Bottom();
            }
            else if (sscpIn instanceof SSCPValue.Constant)
            {
                newResult = sscpIn; // TODO ??
            }
            else
            {
                newResult = new SSCPValue.Top();
            }

            if (oldResult != newResult)
            {
                map.put(outValue, newResult);
                workList.add(outValue);
            }
        }
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        if (inValue instanceof Immediate)
        {
            boolean leftImm = Boolean.parseBoolean(inValue.getId());
            SSCPValue val = new SSCPValue.Constant(leftImm);

            map.put(outValue, val);
            workList.add(outValue);
        }
        else if (inValue instanceof Local)
        {
            if (((Local)inValue).isParam())
            {
                map.put(outValue, new SSCPValue.Bottom());
                workList.add(outValue);
            }
        }
        else
        {
            map.put(outValue, new SSCPValue.Top());
        }
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register outReg = (Register) outValue;

        list.add(new Mov(outReg, inValue));

        return list;
    }
}
