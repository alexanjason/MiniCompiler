package llvm.inst;

import arm.Movt;
import arm.Movw;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Register;

import java.util.List;

public interface Instruction {

    String getString();

    List<arm.Instruction> getArm();

    default Register ImmediateToRegister(Immediate immVal, List<arm.Instruction> list)
    {
        Register temp = new Register(new i32());
        Immediate immlower = new Immediate("lower16: " + immVal.getString(), new i32());
        Immediate immupper = new Immediate("upper16: " + immVal.getString(), new i32());
        list.add(new Movw(temp, immlower));
        list.add(new Movt(temp, immupper));
        return temp;
    }

}
