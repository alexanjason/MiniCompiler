package llvm.inst;

import arm.Movt;
import arm.Movw;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.io.PrintStream;
import java.util.*;

public interface Instruction {

    String getString();

    List<arm.Instruction> getArm();

    void sscpInit(Map<Value, SSCPValue> map, List<Value> workList);

    void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList);

    void sscpReplace(Value v, Immediate constant);

    boolean checkRemove(ListIterator list);

    default Register ImmediateToRegister(Immediate immVal, List<arm.Instruction> list)
    {
        Register temp = new Register(new i32());
        String immStr = immVal.getString();
        if (immStr.equals("null"))
        {
            immStr = "0";
        }
        Immediate immlower = new Immediate("#" + immStr, new i32());
        Immediate immupper = new Immediate("#" + immStr, new i32());
        list.add(new Movw(temp, immlower));
        list.add(new Movt(temp, immupper));
        return temp;
    }

    default void print(PrintStream stream)
    {
        stream.println("\t" + getString());
    }

    void replace(Value oldV, Value newV);

    void localValueNumbering(LocalValueNumbering lvn);

}
