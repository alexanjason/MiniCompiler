package llvm.inst;

import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Add implements Instruction {

    Value result;
    Value left;
    Value right;

    public Add(Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;

        result.addDef(this);
        left.addUse(this);
        right.addUse(this);
    }

    public void replace(Value oldV, Value newV)
    {
        if (left == oldV)
        {
            left.getUses().remove(this);
            left = newV;
        }
        if (right == oldV)
        {
            right.getUses().remove(this);
            right = newV;
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
        if (!(lvn.isInMap(left.getId())))
        {
            lvn.enterInMap(left.getId(), left);
        }

        int leftNum = lvn.getNumbering(left.getId());

        if (!(lvn.isInMap(right.getId())))
        {
            lvn.enterInMap(right.getId(), right);
        }

        int rightNum = lvn.getNumbering(right.getId());

        String res1 = "+," + leftNum + "," + rightNum;
        String res2 = "+," + rightNum + "," + leftNum;

        if (lvn.isInMap(res1))
        {
            Value lvnVal = lvn.getVal(res1);
            List<Instruction> list = new ArrayList<>(result.getUses());
            for (Instruction inst: list)
            {
                inst.replace(result, lvnVal);
            }
        }
        else
        {
            lvn.enterInMap(res1, result);
            lvn.enterInMap(res2, result);
        }
    }

    public boolean checkRemove(ListIterator list)
    {
        if (result.isMarked())
        {
            list.remove();
            // TODO update?
            return true;
        }
        return false;
    }

    public String getString()
    {
        return (result.getString() + " = add " + result.getType().getString() + " " + left.getString()
                + ", " + right.getString());
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (left == v)
        {
            left.getUses().remove(this);
            left = constant;
        }
        if (right == v)
        {
            right.getUses().remove(this);
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
                sscpRight = new SSCPValue.Constant(Integer.parseInt(right.getId()));
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
                newResult = new SSCPValue.Constant(l + r);
            }
            else
            {
                newResult = new SSCPValue.Top();
            }

            if (oldResult != newResult)
            {
                map.put(result, newResult);
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
           SSCPValue val = new SSCPValue.Constant(leftImm + rightImm);

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
           System.err.println("sscpinit add. left: " + left.getString() + " right: " + right.getString());
       }
   }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r1 = (Register) result;

        if (left instanceof Immediate)
        {
            Value temp = left;
            left = right;
            right = temp;
        }

        if (left instanceof Immediate)
        {
            Register r2 = ImmediateToRegister((Immediate)left, list);
            list.add(new arm.Add(r1, r2, right));
        }
        else if (left instanceof Register)
        {
            list.add(new arm.Add(r1, (Register)left, right));
        }
        else if (left instanceof Local)
        {
            list.add(new arm.Add(r1, (Local)left, right));
        }
        else
        {
            System.err.println("Add left error");
        }

        return list;
    }

}