package llvm.inst;

import arm.*;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class Icmp implements Instruction {

    private Value result;
    private String cond;
    private Value op1;
    private Value op2;

    public Icmp(Value result, String cond, Value op1, Value op2) {
        this.result = result;
        this.cond = cond;
        this.op1 = op1;
        this.op2 = op2;

        result.addDef(this);
        op1.addUse(this);
        op2.addUse(this);
    }

    public String getString()
    {
        return (result.getString() + " = icmp " + cond + " " +
                op1.getType().getString() + " " + op1.getString() + ", " + op2.getString());
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
                sscpLeft = new SSCPValue.Constant(Integer.parseInt(op1.getId()));
            }
            else
            {
                sscpLeft = map.get(op1);
            }

            if (op2 instanceof Immediate)
            {
                sscpRight = new SSCPValue.Constant(Integer.parseInt(op2.getId()));
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
                int l = (int)((SSCPValue.Constant) sscpLeft).getConst();
                int r = (int)((SSCPValue.Constant) sscpRight).getConst();
                if (cond.equals("slt"))
                {
                    newResult = new SSCPValue.Constant(l < r);
                }
                else if (cond.equals("sgt"))
                {
                    newResult = new SSCPValue.Constant(l > r);
                }
                else if (cond.equals("sle"))
                {
                    newResult = new SSCPValue.Constant(l <= r);
                }
                else if (cond.equals("sge"))
                {
                    newResult = new SSCPValue.Constant(l >= r);
                }
                else if (cond.equals("eq"))
                {
                    newResult = new SSCPValue.Constant(l == r);
                }
                else if (cond.equals("ne"))
                {
                    newResult = new SSCPValue.Constant(l != r);
                }
                else
                {
                    System.err.println("PANIC icmp sscp init");
                    newResult = null;
                }
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
        if ((op1 instanceof Immediate) && (op2 instanceof Immediate))
        {
            int leftImm = Integer.parseInt(op1.getId());
            int rightImm = Integer.parseInt(op2.getId());

            SSCPValue val;
            if (cond.equals("slt"))
            {
                val = new SSCPValue.Constant(leftImm < rightImm);
            }
            else if (cond.equals("sgt"))
            {
                val = new SSCPValue.Constant(leftImm > rightImm);
            }
            else if (cond.equals("sle"))
            {
                val = new SSCPValue.Constant(leftImm <= rightImm);
            }
            else if (cond.equals("sge"))
            {
                val = new SSCPValue.Constant(leftImm >= rightImm);
            }
            else if (cond.equals("eq"))
            {
                val = new SSCPValue.Constant(leftImm == rightImm);
            }
            else if (cond.equals("ne"))
            {
                val = new SSCPValue.Constant(leftImm != rightImm);
            }
            else
            {
                System.err.println("PANIC icmp sscp init");
                val = null;
            }

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
            System.err.println("sscpinit icmp. left: " + op1.getString() + " right: " + op2.getString());
        }
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register resultReg = (Register) result;

        list.add(new Mov(resultReg, new Immediate("0", new i32())));

        //System.out.println("Icmp -> op1 " + op1.getString());

        if (op1 instanceof Immediate)
        {
            Register op1Reg = ImmediateToRegister((Immediate)op1, list);
            list.add(new Cmp(op1Reg, op2));
        }
        else if (op1 instanceof Register)
        {
            list.add(new Cmp((Register)op1, op2));
        }
        else if (op1 instanceof Local)
        {
            list.add(new Cmp((Local)op1, op2));
        }
        else
        {
            System.err.println("Icmp op1 error");
        }

        Immediate immTrue = new Immediate("1", new i32());
        if (cond.equals("slt"))
        {
            list.add(new Movlt(resultReg, immTrue));
        }
        else if (cond.equals("sgt"))
        {
            list.add(new Movgt(resultReg, immTrue));
        }
        else if (cond.equals("sle"))
        {
            list.add(new Movle(resultReg, immTrue));
        }
        else if (cond.equals("sge"))
        {
            list.add(new Movge(resultReg, immTrue));
        }
        else if (cond.equals("eq"))
        {
            list.add(new Moveq(resultReg, immTrue));
        }
        else if (cond.equals("ne"))
        {
            list.add(new Movne(resultReg, immTrue));
        }
        else
        {
            System.err.println("PANIC");
        }

        return list;
    }
}
