package cfg;

import llvm.inst.Instruction;
import llvm.inst.Phi;
import llvm.type.Type;
import llvm.value.Immediate;
import llvm.value.Register;
import llvm.value.Value;

import java.io.PrintStream;
import java.util.*;

public class BasicBlock {

    // label
    Label label;

    // list of successors
    List<BasicBlock> successorList;

    // list of predecessors
    List<BasicBlock> predecessorList;

    // list of instructions
    List<Instruction> instructions;

    // Gen Set
    Set<Value> genSet;  // TODO Value or Register?

    // Kill Set
    Set<Value> killSet; // TODO Value or Register?

    // LiveOut Set
    Set<Value> liveOut; // TODO Value or Register?

    // Local mappings for register-based SSA
    private HashMap<String, Value> localMappings;

    private List<Phi> phiInstructions;

    private List<Phi> incompletePhis;

    private List<arm.Instruction> armInstructions;

    private boolean sealed;

    private boolean stackBased;

    BasicBlock(List<BasicBlock> predList, boolean stackBased)
    {
        label = new Label(); // TODO

        successorList = new ArrayList<>();
        predecessorList = predList;

        instructions = new ArrayList<>();
        armInstructions = new ArrayList<>();

        sealed = false;
        this.stackBased = stackBased;

        if (!stackBased)
        {
            localMappings = new HashMap<>();
            phiInstructions = new ArrayList<>();
            incompletePhis = new ArrayList<>();
            genSet = new HashSet<>();
            killSet = new HashSet<>();
            liveOut = new HashSet<>();
        }
    }

    public void addToInterferenceGraph(InterferenceGraph graph)
    {
        Set<Value> liveSet = liveOut;
        System.out.println(this.label.getString() + ":");
        for (int i = armInstructions.size() - 1; i >= 0; i--)
        //for (arm.Instruction i : armInstructions)
        {
            armInstructions.get(i).print(System.err);

            armInstructions.get(i).addToInterferenceGraph(liveSet, graph);
            //i.addToInterferenceGraph(liveSet, graph);

            System.out.print("\tLive: ");
            for (Value v : liveSet)
            {
                System.out.print(v.getString() + " ");
            }
            System.out.println();
        }
    }

    public void replaceRegs(Map<String, Register> map, Set<String> spillSet)
    {
        for (arm.Instruction inst : armInstructions)
        {
            inst.replaceRegs(map, spillSet);
        }
    }

    public void propagatePhis()
    {
        for (Phi phi : phiInstructions)
        {
            phi.propagate(this, predecessorList);
        }
    }

    public void firstPass()
    {
        for (Instruction inst : instructions)
        {
            List<arm.Instruction> armInstList = inst.getArm();
            for (arm.Instruction armInst : armInstList)
            {
                armInstructions.add(armInst);
                armInst.addToGenAndKill(genSet, killSet);
            }
        }

        System.out.println(this.label.getString() + ":");
        System.out.print("\tgenSet: ");
        for (Value v : genSet)
        {
            System.out.print(v.getString() + " ");
        }
        System.out.print("\n\tkillSet: ");
        for (Value v : killSet)
        {
            System.out.print(v.getString() + " ");
        }
        System.out.println();
    }

    public int getLabelId()
    {
        return this.label.getId();
    }

    public void addInstructionAtEnd(Instruction inst)
    {
        int size = instructions.size();
        instructions.add(size - 1, inst);
    }

    public void addArmInstruction(arm.Instruction inst)
    {
        armInstructions.add(inst);
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
                }
            }
        }
    }

    private void printMappings()
    {
        System.out.println("MAPPING " + label.getString() + " :");

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
            if (!stackBased && llvm)
            {
                for (Phi inst : phiInstructions)
                {
                    inst.print(stream);
                }
            }
            if (llvm)
            {
                for (Instruction inst : instructions)
                {
                    inst.print(stream);
                }
            }
            else
            {
                for (arm.Instruction inst : armInstructions)
                {
                    inst.print(stream);
                }
            }
        }
    }

    public void addInstruction(Instruction inst)
    {
        instructions.add(inst);
    }

}
