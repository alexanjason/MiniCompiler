package llvm.inst;

import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Mult implements Instruction {

    Value result;
    Value left;
    Value right;

    public Mult(Value result, Value left, Value right)
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
        if (lvn.isInMap(left.getId()))
        {
            //left.getUses().remove(this);
            // left = lvn.getVal(left.getId());

            //left.addUse(this);
        }
        else
        {
            lvn.enterInMap(left.getId(), left);
        }

        int leftNum = lvn.getNumbering(left.getId());

        if (lvn.isInMap(right.getId()))
        {
            //right.getUses().remove(this);
            //right = lvn.getVal(right.getId());

            //right.addUse(this);
        }
        else
        {
            lvn.enterInMap(right.getId(), right);
        }

        int rightNum = lvn.getNumbering(right.getId());

        String res1 = "*," + leftNum + "," + rightNum;
        String res2 = "*," + rightNum + "," + leftNum;

        //System.out.println(res1 + " -> " + left.getString() + " " + right.getString());


        if (lvn.isInMap(res1))
        {
            //result.getUses().remove(this);
            Value lvnVal = lvn.getVal(res1);
            //result.addUse(this);

            List<Instruction> list = new ArrayList<>(result.getUses());
            for (Instruction inst: list)
            {
                //System.out.println("replacing " + result.getString() + " with " + lvnVal.getString());
                inst.replace(result, lvnVal);
            }
        }
        else
        {
            lvn.enterInMap(res1, result);
            lvn.enterInMap(res2, result);
        }
    }

    public String getString()
    {
        return (result.getString() + " = mul " + result.getType().getString() + " " +
                left.getString() + ", " + right.getString());
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
                newResult = new SSCPValue.Constant(l * r);
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
        if ((left instanceof Immediate) && (right instanceof Immediate))
        {
            int leftImm = Integer.parseInt(left.getId());
            int rightImm = Integer.parseInt(right.getId());
            SSCPValue val = new SSCPValue.Constant(leftImm * rightImm);

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
            System.err.println("sscpinit nult. left: " + left.getString() + " right: " + right.getString());
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
            r3 = ImmediateToRegister((Immediate)right, list);
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
            System.err.println("Mult right error");
        }

        if (left instanceof Immediate)
        {
            Register r2 = ImmediateToRegister((Immediate)left, list);
            if (rightLocal)
            {
                list.add(new arm.Mul(r1, (Local)right, r2));
            }
            else
            {
                list.add(new arm.Mul(r1, r2, r3));
            }
        }
        else if (left instanceof Register)
        {
            if (rightLocal)
            {
                list.add(new arm.Mul(r1, (Local)right, (Register) left));
            }
            else
            {
                list.add(new arm.Mul(r1, (Register)left, r3));
            }
        }
        else if (left instanceof Local)
        {
            if (rightLocal)
            {
                list.add(new arm.Mul(r1, (Local)left, (Local)right));
            }
            else
            {
                list.add(new arm.Mul(r1, (Local)left, r3));
            }
        }

        return list;
    }
}
