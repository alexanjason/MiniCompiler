package llvm.inst;

import arm.Ldr;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Load implements Instruction {

    Value result;
    Value pointer;

    public Load(Value result, Value pointer) {
        this.result = result;
        this.pointer = pointer;

        result.addDef(this);
        pointer.addUse(this);
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (pointer == v)
        {
            pointer = constant;
        }
    }

    public boolean checkRemove(ListIterator list)
    {
        if (result.isMarked())
        {
            list.remove();
            return true;
        }
        return false;
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        if (!(map.get(result) instanceof SSCPValue.Bottom))
        {
            SSCPValue oldResult = map.get(result);
            SSCPValue newResult;

            SSCPValue sscpPtr;

            if (pointer instanceof Immediate)
            {
                sscpPtr = new SSCPValue.Constant(Integer.parseInt(pointer.getId()));
            }
            else
            {
                sscpPtr = map.get(pointer);
            }

            if (sscpPtr instanceof SSCPValue.Bottom)
            {
                newResult = new SSCPValue.Bottom();
            }
            else if (sscpPtr instanceof SSCPValue.Constant)
            {
                newResult = sscpPtr;
            }
            else
            {
                newResult = new SSCPValue.Top();
            }

            if (oldResult != newResult)
            {
                workList.add(result);
                map.put(result, newResult);
            }
        }
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        if (pointer instanceof Immediate)
        {
            int ptrImm = Integer.parseInt(pointer.getId());
            SSCPValue val = new SSCPValue.Constant(ptrImm);

            map.put(result, val);
            workList.add(result);
        }
        else if (pointer instanceof Local)
        {
            if (((Local)pointer).isParam())
            {
                map.put(result, new SSCPValue.Bottom());
                workList.add(result);
            }
            else
            {
                map.put(result, new SSCPValue.Top());
            }
        }

        else if ((pointer instanceof Register) || (pointer instanceof StackLocation))
        {
            map.put(result, new SSCPValue.Top());
        }
        else
        {
            System.err.println("sscpinit load. pointer: " + pointer.getString());
        }
    }

    public String getString()
    {
        String typeStr = result.getType().getString();
        return (result.getString() + " = load " + typeStr + ", " + typeStr + "* " + pointer.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register resultReg = (Register) result;
        Register ptrReg = (Register) pointer;
        list.add(new Ldr(resultReg, ptrReg));

        return list;
    }

}
