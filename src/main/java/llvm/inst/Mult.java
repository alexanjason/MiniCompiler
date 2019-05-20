package llvm.inst;

import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Mult implements Instruction {

    Value result;
    Value left;
    Value right;

    public Mult(Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;
    }

    public String getString()
    {
        return (result.getString() + " = mul " + result.getType().getString() + " " +
                left.getString() + ", " + right.getString());
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
