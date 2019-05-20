package llvm.inst;

import arm.*;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

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
    }

    public String getString()
    {
        return (result.getString() + " = icmp " + cond + " " +
                op1.getType().getString() + " " + op1.getString() + ", " + op2.getString());
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
