package llvm.inst;

import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class Xor implements Instruction {

    Value op1;
    Value op2;
    Value result;

    public Xor(Value op1, Value op2, Value result)
    {
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;

        result.addDef(this);
        op1.addUse(this);
        op2.addUse(this);
    }

    public String getString()
    {
        return (result.getString() + " = xor " + result.getType().getString() + " " + op1.getString() + ", " + op2.getString());
    }

    public void replace(Value oldV, Value newV)
    {
        if (op1 == oldV)
        {
            op1.getUses().remove(this);
            op1 = newV;
        }
        if (op2 == oldV)
        {
            op2.getUses().remove(this);
            op2 = newV;
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
        if (!(lvn.isInMap(op1.getId())))
        {
            lvn.enterInMap(op1.getId(), op1);
        }

        int leftNum = lvn.getNumbering(op1.getId());

        if (!(lvn.isInMap(op2.getId())))
        {
            lvn.enterInMap(op2.getId(), op2);
        }

        int rightNum = lvn.getNumbering(op2.getId());

        String res1 = "^," + leftNum + "," + rightNum;
        String res2 = "^," + rightNum + "," + leftNum;

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
            return true;
        }
        return false;
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        if (op1 == v)
        {
            op1.getUses().remove(this);
            op1 = constant;
        }
        if (op2 == v)
        {
            op2.getUses().remove(this);
            op2 = constant;
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

            if (op1 instanceof Immediate)
            {
                sscpLeft = new SSCPValue.Constant(Boolean.parseBoolean(op1.getId()));
            }
            else
            {
                sscpLeft = map.get(op1);
            }

            if (op2 instanceof Immediate)
            {
                sscpRight = new SSCPValue.Constant(Boolean.parseBoolean(op2.getId()));
            }
            else
            {
                sscpRight = map.get(op2);
            }

            if (sscpLeft instanceof SSCPValue.Bottom || sscpRight instanceof SSCPValue.Bottom)
            {
                newResult = new SSCPValue.Bottom();
            }
            else if (sscpLeft instanceof SSCPValue.Constant && sscpRight instanceof SSCPValue.Constant)
            {
                boolean l = (boolean)((SSCPValue.Constant) sscpLeft).getConst();
                System.out.println("sscpRight: " + sscpRight.getString());
                boolean r = (boolean)((SSCPValue.Constant) sscpRight).getConst();
                newResult = new SSCPValue.Constant(l ^ r);
            }
            else
            {
                newResult = new SSCPValue.Top();
            }

            if (oldResult != newResult)
            {
                map.put(result,  newResult);
                workList.add(result);
            }
        }
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        if ((op1 instanceof Immediate) && (op2 instanceof Immediate))
        {
            int leftImm = Integer.parseInt(op1.getId());
            int rightImm = Integer.parseInt(op2.getId());
            SSCPValue val = new SSCPValue.Constant(leftImm ^ rightImm);

            map.put(result, val);
            workList.add(result);
        }
        else if (op1 instanceof Local)
        {
            if (((Local)op1).isParam())
            {
                map.put(result, new SSCPValue.Bottom());
                workList.add(result);
            }
        }
        else if (op2 instanceof Local)
        {
            if (((Local)op2).isParam())
            {
                map.put(result, new SSCPValue.Bottom());
                workList.add(result);
            }
        }
        else if ((op1 instanceof Register) || (op1 instanceof StackLocation) || (op2 instanceof Register) || (op2 instanceof StackLocation))
        {
            map.put(result, new SSCPValue.Top());
        }
        else
        {
            System.err.println("sscpinit xor. op1: " + op1.getString() + " op2: " + op2.getString());
        }
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r1 = (Register) result;

        Register r2;

        if (op1 instanceof Immediate)
        {
            r2 = ImmediateToRegister((Immediate)op1, list);
        }
        else
        {
            // TODO stack location
            r2 = (Register) op1;
        }

        list.add(new arm.Eor(r1, r2, op2));

        return list;
    }
}
