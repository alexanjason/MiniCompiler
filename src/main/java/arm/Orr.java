package arm;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

public class Orr implements Instruction {

    Register r1;
    Register r2;
    Value Operand2;

    public Orr(Register r1, Register r2, Value operand2)
    {
        this.r1 = r1;
        this.r2 = r2;
        Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("orr " + r1.getString() + ", " + r2.getString() + ", ");
        if (Operand2 instanceof Immediate)
        {
            sb.append("#");
        }
        sb.append(Operand2.getString());
        return sb.toString();
    }
}
