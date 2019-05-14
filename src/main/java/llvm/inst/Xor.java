package llvm.inst;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Xor implements Instruction {

    Value op1;
    Value op2;
    Value result;

    public Xor(Value op1, Value op2, Value result)
    {
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
    }

    public String getString()
    {
        return (result.getString() + " = xor " + result.getType().getString() + " " + op1.getString() + ", " + op2.getString());
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
            // TODO stack location???
            r2 = (Register) op1;
        }

        list.add(new arm.Eor(r1, r2, op2));

        return list;
    }
}
