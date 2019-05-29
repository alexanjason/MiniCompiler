package llvm.inst;

import arm.Mov;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;

public class FuncStart implements Instruction{

    List<String> params;

    public FuncStart(List<String> list)
    {
        params = list;
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
        System.err.println("sscpEval funcstart");
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
        // TODO this is a hack

        List<arm.Instruction> instList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("fp");
        list.add("lr");
        instList.add(new arm.Push(list));

        // TODO this is bizarre
        Register fp = new Register(new i32(), 11);
        Register sp = new Register(new i32(), 13);
        Value imm = new Immediate("4", new i32());

        instList.add(new arm.Add(fp, sp, imm));

        int i = 0;
        for (String s : params)
        {
            instList.add(new Mov(new Local(s, new i32()), new Register(new i32(), i)));
            i++;
        }
        // TODO more than 3 params

        return instList;
    }
}
