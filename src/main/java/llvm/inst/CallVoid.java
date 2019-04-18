package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

import java.util.List;

public class CallVoid implements Instruction {

    Type retType;
    String name;
    List<Type> paramTypes;
    List<Value> paramVals;

    public CallVoid(Type retType, String name, List<Type> paramTypes, List<Value> paramVals)
    {
        this.retType = retType;
        this.name = name;
        this.paramTypes = paramTypes;
        this.paramVals = paramVals;
    }

    public String getString()
    {
        int size = paramVals.size();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("call " + retType.getString() + " @" + name + "(");
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
