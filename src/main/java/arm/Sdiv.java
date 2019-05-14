package arm;

import llvm.value.Register;

public class Sdiv implements Instruction {

    Register r1;
    Register r2;
    Register r3;

    public Sdiv(Register r1, Register r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public String getString()
    {
        return ("sdiv " + r1.getString() + ", " + r2.getString() + ", " + r3.getString());
    }
}