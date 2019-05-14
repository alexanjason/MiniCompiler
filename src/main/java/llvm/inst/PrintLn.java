package llvm.inst;

import arm.Bl;
import arm.Mov;
import llvm.value.Register;
import llvm.value.Value;
import llvm.type.Type;

import java.util.ArrayList;
import java.util.List;

public class PrintLn implements Instruction {

    Value val;
    Type type;

    public PrintLn(Value val, Type type)
    {
        this.val = val;
        this.type = type;
    }

    public String getString()
    {
        return ("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), " +
                type.getString() + " " + val.getString() + ")");
    }

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        list.add(new Mov(new Register(val.getType(), 0), val));
        list.add(new Bl("println"));
        return list;
    }

}
