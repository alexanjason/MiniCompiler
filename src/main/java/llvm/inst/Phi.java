package llvm.inst;

import arm.Mov;
import cfg.BasicBlock;
import cfg.Label;
import llvm.type.Type;
import llvm.type.i32;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;

import java.util.ArrayList;
import java.util.List;


public class Phi implements Instruction {

    Value result;
    List<PhiEntry> entryList;
    String id;

    static int increment = 0; // TODO

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

    public List<arm.Instruction> getArm()
    {
        // nah
        // TODO dumb
        return new ArrayList<>();
    }

    public void propagate(BasicBlock curBlock, List<BasicBlock> predList)
    {
        // propagate up to predecessors
        for (PhiEntry entry : entryList)
        {
            for (BasicBlock b : predList)
            {
                if (b.getLabelId() == entry.label.getId())
                {
                    Local phi = new Local("_phi" + increment, new i32());
                    increment++;

                    Instruction prevMove = new Move(phi, entry.value);
                    b.addInstructionAtEnd(prevMove);

                    arm.Instruction mov;
                    if (result instanceof Local)
                    {
                        mov = new Mov((Local)result, phi);
                    }
                    else if (result instanceof Register)
                    {
                        mov = new Mov((Register)result, phi);
                    }
                    else {

                        System.err.println(result.getString() + " " + result.getType());
                        mov = null;
                        System.err.println("phi inst get arm PANIC");
                    }
                    curBlock.addArmInstruction(mov);
                }
            }
        }
    }
}
