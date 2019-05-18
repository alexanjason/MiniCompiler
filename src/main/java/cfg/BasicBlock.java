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

    private  List<Phi> phiInstructions;

    private List<Phi> incompletePhis;

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
        /*
        System.out.println(label.getString() + ": writeVariable: " + id + " " + value.getString() + " " + value);
        */
        localMappings.put(id, value);
    }

    // TODO pass type through for creating phi instructions?
    public Value readVariable(String id, Type type)
    {
        if (localMappings.containsKey(id))
        {
            //System.out.println("Found in local mapping: " + id);
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
            /*
            System.out.println(label.getString() + " block not sealed: " + id);
            */
            //val = new Local(id, type);
            Register reg = new Register(type);
            val = reg;
            Phi phi = new Phi(val, id);
            reg.addDef(phi);
            /*
            System.out.println(label.getString() + " adding to incomplete phis: " + phi.getString());
            */
            incompletePhis.add(phi);
        }
        else if (predecessorList.size() == 0)
        {
            /*
            System.out.println(label.getString() + " preds = 0 : " + id);
            */

            // TODO set val to default of type
            val = new Immediate(type.getDefault(), type);//new Local(type.getDefault(), type);
            //val = new Register(type);
            System.err.println("Uninitialized value: " + id);
            //System.exit(-2);
        }
        else if (predecessorList.size() == 1)
        {
            /*
            System.out.println(label.getString() + " preds = 1 : " + id);
            */

            val = predecessorList.get(0).readVariable(id, type);
        }
        else
        {
            /*
            System.out.println(label.getString() + " preds = *** : " + id);
            */
            for (BasicBlock bb : predecessorList)
            {
                if (bb != null)
                {
                    System.out.println(bb.label.getString());
                }
                else
                {
                    System.out.println("null");
                }
            }
            //val = new Local(id, type);
            Register reg = new Register(type);
            val = reg;
            Phi phi = new Phi(val, id);
            reg.addDef(phi);
            phiInstructions.add(phi);
            writeVariable(id, val);
            addPhiOperands(id, type, phi);
        }
        writeVariable(id, val);
        return val;
    }

    private void addPhiOperands(String id, Type type, Instruction inst)
    {
        Phi phi = (Phi) inst;
        for (BasicBlock pred : predecessorList)
        {
            /*
            System.out.println(label.getString() + ": addPhiOperands " + id + " pred: " + pred);
            */
            phi.addEntry(pred.readVariable(id, type), pred.label);
        }
    }

    public void seal()
    {
        if (!stackBased)
        {
            if (sealed)
            {
                System.err.println(label.getString() + " already sealed");
            }
            else
            {
                /*
                System.out.println("sealing: " + label.getString());
                */
                for (Phi phi : incompletePhis)
                {
                    addPhiOperands(phi.getId(), phi.getType(), phi);
                    /*
                    //Phi phi = (Phi) inst;
                    System.out.println(phi.getString());
                    for (Value val : phi.getVals())
                    {
                        System.out.println(val.getString());
                        addPhiOperands(val.getString(), val.getType(), phi);
                    }
                    */
                    phiInstructions.add(phi);
                }
                sealed = true;
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

    public void print(PrintStream stream)
    {
        /*
        if (!stackBased) {
            printMappings();
        }
        */

        // TODO debug to get rid of empty blocks so this isn't necessary
        if (instructions.size() != 0)
        {
            stream.println(label.getString() + ":");
            if (!stackBased)
            {
                for (Instruction inst : phiInstructions)
                {
                    stream.println("\t" + inst.getString());
                }
            }
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
