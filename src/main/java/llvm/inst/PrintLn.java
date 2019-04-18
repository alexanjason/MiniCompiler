package llvm.inst;

import llvm.value.Value;
import llvm.type.Type;

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

}
