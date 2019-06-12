package llvm.inst;

import arm.Mov;
import cfg.BasicBlock;
import cfg.Label;
import cfg.LocalValueNumbering;
import cfg.SSCPValue;
import llvm.type.Type;
import llvm.type.i32;
import llvm.value.*;

import java.util.*;


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

        result.addDef(this);
    }

    public boolean checkRemove(ListIterator list)
    {
        if (result.isMarked())
        {
            list.remove();
            return true;
        }
        return false;
    }

    public void sscpReplace(Value v, Immediate constant)
    {
        for (PhiEntry entry : entryList)
        {
            if (entry.value == v)
            {
                v.getUses().remove(this);
                entry.value = constant;
            }
        }
    }

    public void replace(Value oldV, Value newV)
    {
        for (PhiEntry entry : entryList)
        {
            if (entry.value == oldV)
            {
                oldV.getUses().remove(this);
                entry.value = newV;
            }
        }
    }

    public void localValueNumbering(LocalValueNumbering lvn)
    {
    }

    public SSCPValue meet(SSCPValue sscpVal1, Value v, Map<Value, SSCPValue> map)
    {
        SSCPValue sscpVal2;

        if (v instanceof Immediate)
        {
            if (v.getId().equals("true"))
            {
                sscpVal2 = new SSCPValue.Constant(true);
            }
            else if (v.getId().equals("false"))
            {
                sscpVal2 = new SSCPValue.Constant(false);
            }
            else if (v.getId().equals("null"))
            {
                sscpVal2 = new SSCPValue.Constant("null");
            }
            else
            {
                sscpVal2 = new SSCPValue.Constant(Integer.parseInt(v.getId()));
            }
        }
        else
        {
            sscpVal2 = map.get(v);
        }

        if (sscpVal1 instanceof SSCPValue.Bottom || sscpVal2 instanceof SSCPValue.Bottom)
        {
            return new SSCPValue.Bottom();
        }
        else if (sscpVal1 instanceof SSCPValue.Constant)
        {
            return sscpVal1;
        }
        else if (sscpVal2 instanceof SSCPValue.Constant)
        {
            return sscpVal2;
        }
        else
        {
            return new SSCPValue.Top();
        }

    }

    public void sscpEval(Map<Value, SSCPValue> map, ListIterator<Value> workList)
    {
        SSCPValue oldRes = map.get(result);
        SSCPValue newRes = oldRes;//new SSCPValue.Top();

        //boolean hacky = false;

        for (PhiEntry entry : entryList)
        {
            Value v = entry.value;
            SSCPValue oldnew = newRes;
            newRes = meet(newRes, v, map);
            //System.out.println(result.getString() + " = " + oldnew.getString() + " and " + v.getString() + " " + "-> meets at " + newRes.getString());
            /*
            if (v == result)
            {
                hacky = true;
            }
            */
        }

        if (!(oldRes.getString().equals(newRes.getString())))
        {
            //System.out.println("oldRes: " + oldRes.getString() + " newRes: " + newRes.getString());
            //if (!hacky)
            //{
                workList.add(result);
            //}
            //System.err.println("adding " + result.getString() + " to worklist");
            map.put(result, newRes);
        }
    }

    public void sscpInit(Map<Value, SSCPValue> map, List<Value> workList)
    {
        Immediate imm = null;
        Local param = null;

        for (PhiEntry entry : entryList)
        {
            Value v = entry.value;
            if (v instanceof Immediate)
            {
                if (imm == null)
                {
                    imm = (Immediate) v;
                }
                else if (imm.getId().equals((v).getId()))
                {
                    // imm already equals V
                }
                else
                {
                    map.put(result, new SSCPValue.Bottom());
                    workList.add(result);
                    return;
                }
            }
            else if (v instanceof Local)
            {
                if (((Local)v).isParam())
                {
                    param = (Local) v;
                }
            }
        }
        if (param != null)
        {
            map.put(result, new SSCPValue.Bottom());
            workList.add(result);
            return;
        }
        else if (imm != null)
        {
            SSCPValue sscpConst;
            if (imm.getId().equals("null"))
            {
                sscpConst = new SSCPValue.Constant("null");
            }
            else
            {
                int immV = Integer.parseInt(imm.getId());
                sscpConst = new SSCPValue.Constant(immV);
            }
            map.put(result, sscpConst);
            workList.add(result);
            return;
        }
        else
        {
            map.put(result, new SSCPValue.Top());
            return;
        }
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
        val.addUse(this);
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
