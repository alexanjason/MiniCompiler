package llvm.inst;

import arm.Movt;
import arm.Movw;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Register;

import java.io.PrintStream;
import java.util.List;

public interface Instruction {

    String getString();

    List<arm.Instruction> getArm();

    default Register ImmediateToRegister(Immediate immVal, List<arm.Instruction> list)
    {
        Register temp = new Register(new i32());
        String immStr = immVal.getString();
        if (immStr.equals("null"))
        {
            immStr = "0";
        }
        Immediate immlower = new Immediate("lower16: " + immStr, new i32());
        Immediate immupper = new Immediate("upper16: " + immStr, new i32());
        list.add(new Movw(temp, immlower));
        list.add(new Movt(temp, immupper));
        return temp;
    }

    /*
    default String stringArm()
    {
        StringBuilder sb = new StringBuilder();
        for (arm.Instruction inst : this.getArm())
        {
            sb.append(inst.getString());
        }
        return sb.toString();
    }
    */

    default void print(PrintStream stream, boolean llvm)
    {
        if (llvm)
        {
            stream.println("\t" + getString());
        }
        else
        {
            for (arm.Instruction inst : this.getArm())
            {
                stream.println("\t" + inst.getString());
            }
        }
    }

}
