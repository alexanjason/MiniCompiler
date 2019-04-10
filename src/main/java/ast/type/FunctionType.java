package ast.type;

import ast.Function;

import java.util.List;

public class FunctionType implements Type {
    private List<Type> paramTypes;
    private Type returnType;

    public FunctionType(List<Type> params, Type ret)
    {
        this.paramTypes = params;
        this.returnType = ret;
    }

    public boolean compareType(Type type)
    {
        // TODO compare params and ret type?
        return type instanceof FunctionType;
    }

    public int numParams()
    {
        return paramTypes.size();
    }

    public Type getParamType(int i)
    {
        return paramTypes.get(i);
    }

    public Type getReturnType()
    {
        return returnType;
    }
}
