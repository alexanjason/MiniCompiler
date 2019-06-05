package llvm.inst;

import arm.Mov;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Trunc implements Instruction {

    Value outValue;
    Value inValue;

    public Trunc(Value inValue, Value outValue)
    {
        this.outValue = outValue;
        this.inValue = inValue;

        outValue.addDef(this);
        inValue.addUse(this);
    }

    public String getString()
    {
        return (outValue.getString() + " = trunc " + inValue.getType().getString() + " " + inValue.getString() + " to " + outValue.getType().getString());
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
            inValue.getUses().remove(this);
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
                sscpIn = new SSCPValue.Constant(Integer.parseInt(inValue.getId()));
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
                //newResult = sscpIn; // TODO ??
                System.out.println("trunc eval " + this.getString() + " -> " + sscpIn.getString());
                int bool = (int)((SSCPValue.Constant) sscpIn).getConst();
                if (bool == 0)
                {
                    newResult = new SSCPValue.Constant(false);
                }
                else
                {
                    newResult = new SSCPValue.Constant(true);
                }
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
            int leftImm = Integer.parseInt(inValue.getId());
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
        Register inReg = (Register) inValue;
        Register outReg = (Register) outValue;
        list.add(new Mov(outReg, inReg));
        return list;
    }
}
