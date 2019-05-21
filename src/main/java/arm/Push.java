package arm;

import llvm.value.Value;

import java.util.List;
import java.util.Set;

public class Push implements Instruction {

    List<String> list;

    public Push(List<String> list)
    {
        this.list = list;
    }

    public String getString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("push {");

        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            builder.append(list.get(i));
            if (i != size - 1)
            {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public void addToGenAndKill(Set<Value> genSet, Set<Value> killSet)
    {
    }
}
