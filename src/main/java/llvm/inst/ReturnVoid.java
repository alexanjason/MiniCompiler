package llvm.inst;

import cfg.SSCPValue;
import llvm.value.*;

import java.util.*;

public class ReturnVoid implements Instruction {

    // ret void

    public String getString()
    {
        return ("ret void");
    }

    public boolean checkRemove(ListIterator list)
    {
        return false;
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        System.err.println("sscpinit returnvoid");
    }

    public void sscpReplace(Value v, Immediate constant)
    {
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        System.err.println("sscpEvalreturnvoid");
    }

    public List<arm.Instruction> getArm()
    {
        return new ArrayList<>();
    }
}
