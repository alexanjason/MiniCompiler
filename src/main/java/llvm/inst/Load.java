package llvm.inst;

import arm.Ldr;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Load implements Instruction {

    Value result;
    Value pointer;

    public Load(Value result, Value pointer) {
        this.result = result;
        this.pointer = pointer;
    }

    public String getString()
    {
        String typeStr = result.getType().getString();
        return (result.getString() + " = load " + typeStr + ", " + typeStr + "* " + pointer.getString());
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        Register resultReg = (Register) result;
        Register ptrReg = (Register) pointer;
        list.add(new Ldr(resultReg, ptrReg));

        return new ArrayList<>();
    }

}
