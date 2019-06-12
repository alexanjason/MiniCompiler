package llvm.inst;

import arm.Pop;
import arm.Push;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class FuncEnd implements Instruction{

    String name;
    int offset;

    public FuncEnd(String name)
    {
        this.name = name;
    }

    public void updateSpills(int spills)
    {
        this.offset = spills;
    }

    public String getString()
    {
        return "";
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit funcend");
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEval funcend");
    }

    public void replace(Value oldV, Value newV)
    {
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public void sscpReplace(Value v, Immediate constant)
    {
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    @Override
    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> instList = new ArrayList<>();

        Register fp = new Register(new i32(), 11); // TODO
        Register sp = new Register(new i32(), 13);

        instList.add(new arm.Add(sp, sp, new Immediate(Integer.toString(offset*4), new i32())));

        List<String> calleeSaved = new ArrayList<>();
        calleeSaved.add("r4");
        calleeSaved.add("r5");
        calleeSaved.add("r6");
        calleeSaved.add("r7");
        calleeSaved.add("r8");
        calleeSaved.add("r9");
        calleeSaved.add("r10");

        instList.add(new Pop(calleeSaved));

        //instList.add(new arm.Sub(sp, fp, new Immediate("4", new i32())));

        List<String> list = new ArrayList<>();
        list.add("fp");
        list.add("pc");
        instList.add(new arm.Pop(list));

        instList.add(new arm.Size(name));
        return instList;
    }
}

