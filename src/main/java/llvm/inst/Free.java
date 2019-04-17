package llvm.inst;

import llvm.value.Value;

public class Free implements Instruction {

    Value ptr;

    public Free(Value ptr)
    {
        this.ptr = ptr;
    }

    public String getString()
    {
        return ("call void @free(i8* " + ptr.getString() + ")");
    }
}
