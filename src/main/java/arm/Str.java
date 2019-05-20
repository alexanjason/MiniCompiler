package arm;

import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

public class Str implements Instruction {

    Value r1;
    Register r2;

    public Str(Register r1, Register r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public Str(Local r1, Register r2)
    {
        this.r1 = r1;
        this.r2 = r2;
    }

    public String getString()
    {
        return ("str " + r1.getString() + ", " + r2.getString());
    }
}
