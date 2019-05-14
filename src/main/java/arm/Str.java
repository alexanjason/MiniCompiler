package arm;

import llvm.value.Register;

public class Str implements Instruction {

    Register r1;
    Register r2; // TODO?

    public Str(Register r1, Register r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public String getString()
    {
        return ("str " + r1.getString() + ", " + r2.getString());
    }
}
