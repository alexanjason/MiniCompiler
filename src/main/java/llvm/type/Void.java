package llvm.type;

import llvm.type.Type;

public class Void implements Type {

    public String getString()
    {
        return "void";
    }

    public String getDefault()
    {
        return "void";
    }
}
