package llvm.inst;

import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Sdiv implements Instruction {

    Value result;
    Value left;
    Value right;

    public Sdiv(Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;
    }

    public String getString()
    {
        return (result.getString() + " = sdiv " + result.getType().getString() + " " + left.getString()
                + ", " + right.getString());
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
