package arm;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

public class Movt implements Instruction {

    Register r1;
    Value Operand2;

    public Movt(Register r1, Value operand2)
    {
        this.r1 = r1;
        Operand2 = operand2;
    }

    public String getString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("movt " + r1.getString() + ", ");
        if (Operand2 instanceof Immediate)
        {
            sb.append("#");
        }
        sb.append(Operand2.getString());
        return sb.toString();
    }
}
