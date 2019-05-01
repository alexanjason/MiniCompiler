package llvm.inst;

import cfg.Label;
import llvm.type.Type;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;


public class Phi implements Instruction {

    Value result;
    List<PhiEntry> entryList;
    String id;

    public Phi(Value result, String id)
    {
        this.result = result;
        this.entryList = new ArrayList<>();
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public Type getType()
    {
        return this.result.getType();
    }

    public List<Value> getVals()
    {
        List<Value> vals = new ArrayList<>();
        for (PhiEntry entry : entryList)
        {
            vals.add(entry.value);
        }
        return vals;
    }

    public void addEntry(Value val, Label label)
    {
        entryList.add(new PhiEntry(val, label));
    }

    public String getString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(result.getString());
        builder.append(" = phi ");
        builder.append(result.getType().getString());

        int i = 1;
        int size = entryList.size();
        for (PhiEntry entry : entryList)
        {
            builder.append(" [");
            builder.append(entry.value.getString());
            builder.append(", %");
            builder.append(entry.label.getString());
            builder.append("]");
            if (i != size)
            {
                builder.append(", ");
            }
            i++;
        }

        return builder.toString();
    }

}
