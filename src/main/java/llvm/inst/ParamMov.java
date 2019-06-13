package llvm.inst;

import arm.Mov;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ParamMov implements Instruction {

    List<String> params;

    public ParamMov(List<String> list)
    {
        params = list;
    }

    public String getString()
    {
        return "";
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
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
