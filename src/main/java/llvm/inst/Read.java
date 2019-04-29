package llvm.inst;

import llvm.value.Value;

public class Read implements  Instruction {

    Value result;

    public Read(Value result)
    {
        this.result = result;
    }

    public String getString()
    {
        //return ("call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.read, i32 0, i32 0), i32* " + result.getString() + ")");
        return (result.getString() + " = call i32 @read_util()");
    }
}
