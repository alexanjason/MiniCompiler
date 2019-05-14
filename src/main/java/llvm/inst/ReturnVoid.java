package llvm.inst;

import java.util.ArrayList;
import java.util.List;

public class ReturnVoid implements Instruction {

    // ret void

    public String getString()
    {
        return ("ret void");
    }

    public List<arm.Instruction> getArm()
    {
        return new ArrayList<>();
    }
}
