package arm;

import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

public class Mov implements Instruction {

    Value r1;
    Value Operand2;

    public Mov(Register r1, Value operand2)
    {
        this.r1 = r1;
        this.Operand2 = operand2;
    }

    public Mov(Local r1, Value operand2)
    {
        this.r1 = r1;
        this.Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("mov " + r1.getString() + ", ");
        if (Operand2 instanceof Immediate)
        {
            sb.append("#");
        }
        sb.append(Operand2.getString());
        return sb.toString();
    }
}
