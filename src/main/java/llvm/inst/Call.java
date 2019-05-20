package llvm.inst;

import arm.Bl;
import arm.Mov;
import llvm.type.Type;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Call implements Instruction {

    Value result;
    String name;
    // TODO should be list of parameter values?
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

    public List<arm.Instruction> getArm()
    {
        List<arm.Instruction> list = new ArrayList<>();
        for (int i = 0; i < paramVals.size(); i++)
        {
            // TODO does stack based still put args into regs?
            //System.out.println("i: " + i + " paramVal " + paramVals.get(i).getString());
            list.add(new Mov(new Register(paramTypes.get(i), i), paramVals.get(i)));
        }
        list.add(new Bl(name));
        Register r = (Register) result;
        list.add(new Mov(r, new Register(result.getType(), 0)));

        return list;
    }
}
