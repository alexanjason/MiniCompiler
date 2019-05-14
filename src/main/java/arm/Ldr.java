package arm;

import llvm.value.Register;

public class Ldr implements Instruction {

    Register r1;
    Register r2; // TODO

    public Ldr(Register r1, Register r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public String getString()
    {
        return ("ldr " + r1.getString() + ", " + r2.getString());
    }
}
