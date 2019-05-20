package llvm.inst;

import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

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

