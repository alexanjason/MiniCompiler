package llvm.inst;

import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Sdiv implements Instruction {

    Value result;
    Value left;
    Value right;

    public Sdiv(Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;

        result.addDef(this);
        left.addUse(this);
        right.addUse(this);
    }

    public String getString()
    {
        return (result.getString() + " = sdiv " + result.getType().getString() + " " + left.getString()
                + ", " + right.getString());
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (left == v)
        {
            left = constant;
        }
        if (right == v)
        {
            right = constant;
        }
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        if (!(map.get(result) instanceof SSCPValue.Bottom))
        {
            SSCPValue oldResult = map.get(result);
            SSCPValue newResult;

            SSCPValue sscpLeft;
            SSCPValue sscpRight;

            if (left instanceof Immediate)
            {
                sscpLeft = new SSCPValue.Constant(Integer.parseInt(left.getId()));
            }
            else
            {
                sscpLeft = map.get(left);
            }

            if (right instanceof Immediate)
            {
                sscpRight = new SSCPValue.Constant(Integer.parseInt(left.getId()));
            }
            else
            {
                sscpRight = map.get(right);
            }

            if (sscpLeft instanceof SSCPValue.Bottom || sscpRight instanceof SSCPValue.Bottom)
            {
                newResult = new SSCPValue.Bottom();
            }
            else if (sscpLeft instanceof SSCPValue.Constant && sscpRight instanceof SSCPValue.Constant)
            {
                int l = (int)((SSCPValue.Constant) sscpLeft).getConst();
                int r = (int)((SSCPValue.Constant) sscpRight).getConst();
                newResult = new SSCPValue.Constant(l / r);
            }
            else
            {
                newResult = new SSCPValue.Top();
            }

            if (oldResult != newResult)
            {
                workList.add(result);
            }
        }
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        if ((left instanceof Immediate) && (right instanceof Immediate))
        {
            int leftImm = Integer.parseInt(left.getId());
            int rightImm = Integer.parseInt(right.getId());
            SSCPValue val;
            if (rightImm == 0)
            {
                val = new SSCPValue.Bottom();
            }
            else
            {
                val = new SSCPValue.Constant(leftImm / rightImm);
            }

            map.put(result, val);
            workList.add(result);
        }
        else if (left instanceof Local)
        {
            if (((Local)left).isParam())
            {
                map.put(result, new SSCPValue.Bottom());
                workList.add(result);
            }
        }
        else if (right instanceof Local)
        {
            if (((Local)right).isParam())
            {
                map.put(result, new SSCPValue.Bottom());
                workList.add(result);
            }
        }
        else if ((left instanceof Register) || (left instanceof StackLocation) || (right instanceof Register) || (right instanceof StackLocation))
        {
            map.put(result, new SSCPValue.Top());
        }
        else
        {
            System.err.println("sscpinit sdiv. left: " + left.getString() + " right: " + right.getString());
        }
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r1 = (Register) result;

        Register r3;
        boolean rightLocal = false;
        if (right instanceof Immediate)
        {
            r3 = ImmediateToRegister((Immediate) right, list);
        }
        else if (right instanceof Register)
        {
            r3 = (Register) right;
        }
        else if (right instanceof Local)
        {
            rightLocal = true;
            r3 = null;
        }
        else
        {
            r3 = null;
            System.err.println("Sdiv right error");
        }

        if (left instanceof Register)
        {
            if (rightLocal)
            {
                list.add(new arm.Sdiv(r1, (Register)left, (Local)right));
            }
            else {
                list.add(new arm.Sdiv(r1, (Register)left, r3));
            }
        }
        else if (left instanceof Local)
        {
            // TODO sus
            if (rightLocal) {
                list.add(new arm.Sdiv(r1, (Local)left, (Local)right));
            }
            else
            {
                list.add(new arm.Sdiv(r1, (Local)left, r3));
            }
        }
        else if (left instanceof Immediate)
        {
            Register r = ImmediateToRegister((Immediate) left, list);
            if (rightLocal) {
                list.add(new arm.Sdiv(r1, r, (Local)right));
            }
            else
            {
                list.add(new arm.Sdiv(r1, r, r3));
            }
        }
        else
        {
            System.err.println("Sdiv left error");
            System.err.println(left.getType().getString() + " " + left.getString());
        }

        return list;
    }
}
