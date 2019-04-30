package cfg;

import llvm.inst.Instruction;
import llvm.inst.Phi;
import llvm.value.Local;
import llvm.value.Register;
import llvm.value.Value;
import llvm.type.Void;

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

    private  List<Instruction> phiInstructions;

    private List<Instruction> incompletePhis;

    private boolean sealed;

    private boolean stackBased;

    BasicBlock(List<BasicBlock> predList, boolean stackBased)
    {
        label = new Label(); // TODO
        successorList = new ArrayList<>();
        predecessorList = predList;//new ArrayList<>();
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

    public void writeVariable(String id, Value value)
    {
        localMappings.put(id, value);
    }

    // TODO pass type through for creating phi instructions?
    public Value readVariable(String id)
    {
        if (localMappings.containsKey(id))
        {
            return localMappings.get(id);
        }
        else
        {
            return readVariableFromPreds(id);
        }
    }

    public Value readVariableFromPreds(String id)
    {
        Value val;
        if (!sealed)
        {
            val = new Local(id, new Void()); //TODO type and local?
            Instruction phi = new Phi(val);
            incompletePhis.add(phi);

            // TODO add to phiInstructions too?
        }
        else if (predecessorList.size() == 0)
        {
            // TODO set val to default of type
            val = new Local("Undefined", new Void());
            System.err.println("Uninitialized value: " + id);
            System.exit(-2);
        }
        else if (predecessorList.size() == 1)
        {
            val = predecessorList.get(0).readVariableFromPreds(id);
        }
        else
        {
            val = new Local(id, new Void()); // TODO type and local?
            Instruction phi = new Phi(val);
            phiInstructions.add(phi);
            writeVariable(id, val);
            addPhiOperands(id, phi);
        }
        writeVariable(id, val);
        return val;

    }

    private void addPhiOperands(String id, Instruction inst)
    {
        Phi phi = (Phi) inst;
        for (BasicBlock pred : predecessorList)
        {
            phi.addEntry(pred.readVariable(id), pred.label);
        }
    }

    public void seal()
    {
        if (!stackBased)
        {
            sealed = true;
            // TODO go through incompletePhis
        }
    }

    public void print(PrintStream stream)
    {
        // TODO debug to get rid of empty blocks so this isn't necessary
        if (instructions.size() != 0)
        {
            stream.println(label.getString() + ":");
            for (Instruction inst : instructions) {
                stream.println("\t" + inst.getString());
            }
        }
    }

    public void addInstruction(Instruction inst)
    {
        instructions.add(inst);
    }

}
