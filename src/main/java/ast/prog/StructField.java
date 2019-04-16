package ast.prog;

import ast.type.Type;

public class StructField {

    protected Type type;
    protected int index;

    public StructField(Type type, int index)
    {
        this.type = type;
        this.index = index;
    }
}
