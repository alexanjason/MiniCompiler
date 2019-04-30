package llvm.inst;

import cfg.Label;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;


public class Phi implements Instruction {

    Value result;
    List<PhiEntry> entryList;

    public Phi(Value result)
    {
        this.result = result;
        this.entryList = new ArrayList<>();
    }

    public void addEntry(Value val, Label label)
    {
        entryList.add(new PhiEntry(val, label));
    }

    public String getString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(result.getString());
        builder.append(" = ");
        builder.append(result.getType().getString());

        for (PhiEntry entry : entryList)
        {
            builder.append(" [");
            builder.append(entry.value.getString());
            builder.append(", ");
            builder.append(entry.label.getString());
            builder.append("]");
        }

        return builder.toString();
    }

}
