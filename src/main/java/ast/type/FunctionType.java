package ast.type;

import java.util.List;

public class FunctionType implements Type {
    private List<Type> paramTypes;
    private Type returnType;

    public FunctionType(List<Type> params, Type ret)
    {
        this.paramTypes = params;
        this.returnType = ret;
    }
}
