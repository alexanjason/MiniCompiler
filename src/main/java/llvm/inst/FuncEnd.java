package llvm.inst;

import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class FuncEnd implements Instruction{

    String name;

    public FuncEnd(String name)
    {
        this.name = name;
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

    public void sscpReplace(Value v, Immediate constant)
    {
    }

    @Override
    public List<arm.Instruction> getArm()
    {
        // TODO this is a hack

        List<arm.Instruction> instList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("fp");
        list.add("pc");
        instList.add(new arm.Pop(list));

        // TODO this is bizarre
        instList.add(new arm.Size(name));
        return instList;
    }
}

