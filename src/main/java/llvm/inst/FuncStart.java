package llvm.inst;

import arm.Push;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class FuncStart implements Instruction{

    int spills;

    public FuncStart()
    {

    }

    public void updateSpills(int spills)
    {
        this.spills = spills;
    }

    public String getString()
    {
        return "";
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        //System.err.println("sscpinit funcend");
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        //System.err.println("sscpEval funcstart");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
    }

    public void replace(Value oldV, Value newV)
    {
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    @Override
    public List<arm.Instruction> getArm()
    {
        // TODO this is a hack

        List<arm.Instruction> instList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("fp");
        list.add("lr");
        instList.add(new arm.Push(list));

        // TODO this is bizarre
        Register fp = new Register(new i32(), 11); // TODO
        Register sp = new Register(new i32(), 13);
        Value imm = new Immediate("4", new i32());

        instList.add(new arm.Add(fp, sp, imm));

        List<String> calleeSaved = new ArrayList<>();
        calleeSaved.add("r4");
        calleeSaved.add("r5");
        calleeSaved.add("r6");
        calleeSaved.add("r7");
        calleeSaved.add("r8");
        calleeSaved.add("r9");
        calleeSaved.add("r10");

        instList.add(new Push(calleeSaved));

        instList.add(new arm.Sub(sp, sp, new Immediate(Integer.toString(this.spills*4), new i32())));

        return instList;
    }
}
