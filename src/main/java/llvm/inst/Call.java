package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

import java.util.List;

public class Call implements Instruction {

    Value result;
    String name;
    List<Type> paramTypes;
    List<Value> paramVals;

    public Call(Value result, String name, List<Type> paramTypes, List<Value> paramVals)
    {
        this.result = result;
        this.name = name;
        this.paramTypes = paramTypes;
        this.paramVals = paramVals;
    }

    public String getString()
    {
        int size = paramVals.size();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(result.getString() + " = call " + result.getType().getString() + " @" + name + "(");
        for (int i = 0; i < size; i++)
        {
            strBuilder.append(paramTypes.get(i).getString());
            strBuilder.append(" ");
            strBuilder.append(paramVals.get(i).getString());

            if (i != size - 1)
            {
                strBuilder.append(", ");
            }
        }
        strBuilder.append(")");
        return strBuilder.toString();
    }
}
