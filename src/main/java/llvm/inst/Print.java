package llvm.inst;

import llvm.value.Value;
import llvm.type.Type;

public class Print implements Instruction {

    Value val;
    Type type;

    public Print(Value val, Type type)
    {
        this.val = val;
        this.type = type;
    }

    public String getString()
    {
        return ("call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.print, i32 0, i32 0), " +
                type.getString() + " " + val.getString() + ")");
    }

}