package arm;

import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

public class Sdiv implements Instruction {

    Register r1;
    Value r2; // TODO sus
    Value r3; // TODO sus

    public Sdiv(Register r1, Register r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Sdiv(Register r1, Local r2, Register r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Sdiv(Register r1, Local r2, Local r3)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
    }

    public Sdiv(Register r1, Register r2, Local r3)
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