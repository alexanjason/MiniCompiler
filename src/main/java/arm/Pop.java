package arm;

import java.util.List;

public class Pop implements Instruction {

    List<String> list;

    public Pop(List<String> list)
    {
        this.list = list;
    }

    public String getString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("pop {");

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
}
