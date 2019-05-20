package cfg;

import llvm.inst.Instruction;
import llvm.inst.Phi;
import llvm.type.Type;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasicBlock {

    // label
    Label label;

    // list of successors
    List<BasicBlock> successorList;

    // list of predecessors
    List<BasicBlock> predecessorList;

    // list of instructions
    List<Instruction> instructions;

    // Local mappings for register-based SSA
    private HashMap<String, Value> localMappings;

    private List<Phi> phiInstructions;

    private List<Phi> incompletePhis;

    private boolean sealed;

    private boolean stackBased;

    BasicBlock(List<BasicBlock> predList, boolean stackBased)
    {
        label = new Label(); // TODO
        successorList = new ArrayList<>();
        predecessorList = predList;
        instructions = new ArrayList<>();
        sealed = false;
        this.stackBased = stackBased;

        if (!stackBased)
        {
            localMappings = new HashMap<>();
            phiInstructions = new ArrayList<>();
            incompletePhis = new ArrayList<>();
        }
    }

    public int getLabelId()
    {
        return this.label.getId();
    }

    public void addInstructionAtEnd(Instruction inst)
    {
        int size = instructions.size();
        //System.out.println("adding to end: " + size + " before " + instructions.get(size-2));
        instructions.add(size - 2, inst);
    }

    public void writeVariable(String id, Value value)
    {
        localMappings.put(id, value);
    }

    // TODO pass type through for creating phi instructions?
    public Value readVariable(String id, Type type)
    {
        if (localMappings.containsKey(id))
        {
            return localMappings.get(id);
        }
        else
        {
            return readVariableFromPreds(id, type);
        }
    }

    public Value readVariableFromPreds(String id, Type type)
    {
        Value val;
        if (!sealed)
        {
            Register reg = new Register(type);
            val = reg;
            Phi phi = new Phi(val, id);
            reg.addDef(phi); // TODO
            incompletePhis.add(phi);
            //System.out.println("adding to incomplete phis " + val.getId() + " " + id);
        }
        else if (predecessorList.size() == 0)
        {
            val = new Immediate(type.getDefault(), type);
            System.err.println("Uninitialized value: " + id);
        }
        else if (predecessorList.size() == 1)
        {
            val = predecessorList.get(0).readVariable(id, type);
        }
        else
        {
            Register reg = new Register(type);
            val = reg;
            Phi phi = new Phi(val, id);
            reg.addDef(phi);    // TODO
            phiInstructions.add(phi);
            //System.out.println("adding in else : " + val.getString() + " " + id);
            writeVariable(id, val);
            addPhiOperands(id, type, phi);
        }
        writeVariable(id, val);
        return val;
    }

    private void addPhiOperands(String id, Type type, Phi inst)
    {
        Phi phi = (Phi) inst;
        for (BasicBlock pred : predecessorList)
        {
            phi.addEntry(pred.readVariable(id, type), pred.label);
        }
    }

    public void seal()
    {
        if (!sealed)
        {
            this.sealed = true;
            if (!stackBased) {
                for (Phi phi : incompletePhis) {
                    addPhiOperands(phi.getId(), phi.getType(), phi);
                    phiInstructions.add(phi);
                    //System.out.println("adding in seal : " + phi.getString());
                }
            }
        }
    }

    private void printMappings()
    {
        System.out.println("MAPPING " + label.getString() + " :");
        //System.out.println("isSealed: " + sealed);
        // private HashMap<String, Value> localMappings;
        for (String key : localMappings.keySet())
        {
            Value v = localMappings.get(key);
            System.out.println("\t" + key + " -> " + v.getString() + " " + v);
        }
    }

    public void print(PrintStream stream, boolean llvm)
    {
        /*
        if (!stackBased) {
            printMappings();
        }
        */

        // TODO debug to get rid of empty blocks so this isn't necessary
        if (instructions.size() != 0)
        {
            if (!llvm)
            {
                stream.print(".");
            }
            stream.println(label.getString() + ":");
            if (!stackBased)
            {
                for (Phi inst : phiInstructions)
                {
                    inst.print(stream, llvm, predecessorList);
                }
            }
            for (Instruction inst : instructions)
            {
                inst.print(stream, llvm);
            }
        }
    }

    public void addInstruction(Instruction inst)
    {
        instructions.add(inst);
    }

}
