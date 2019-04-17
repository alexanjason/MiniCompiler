package cfg;

import llvm.inst.Instruction;

import java.io.PrintStream;
import java.util.ArrayList;
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

    BasicBlock(List<BasicBlock> predList)
    {
        label = new Label(); // TODO
        successorList = new ArrayList<>();
        predecessorList = predList;//new ArrayList<>();
        instructions = new ArrayList<>();
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
