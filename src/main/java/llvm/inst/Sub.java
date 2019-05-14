package llvm.inst;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Sub implements Instruction {

    Value result;
    Value left;
    Value right;

    public Sub (Value result, Value left, Value right)
    {
        this.result = result;
        this.left = left;
        this.right = right;
    }

    public String getString()
    {
        return (result.getString() + " = sub " + result.getType().getString() + " " + left.getString() + ", " + right.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register r1 = (Register) result;

        Register r2;

        if (left instanceof Immediate)
        {
            r2 = ImmediateToRegister((Immediate)left, list);
        }
        else
        {
            // TODO stack location???
            r2 = (Register) left;
        }

        list.add(new arm.Sub(r1, r2, right));

        return list;
    }
}
