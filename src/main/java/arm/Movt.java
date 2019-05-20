package arm;

import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

public class Movt implements Instruction {

    Register r1;
    Immediate imm16;

    public Movt(Register r1, Immediate imm16)
    {
        this.r1 = r1;
        this.imm16 = imm16;
    }

    public String getString()
    {
        return ("movt " + r1.getString() + ", " + imm16.getString());
    }
}
