package llvm.inst;

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
        Register r2 = (Register) left;
        Register r3 = (Register) right;

        // TODO account for immediates
        list.add(new arm.Sdiv(r1, r2, r3));

        return list;
    }
}
