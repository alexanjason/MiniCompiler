package cfg;

import llvm.Instruction;

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
    }

    public void print(PrintStream stream)
    {
        for (Instruction inst : instructions)
        {
            stream.print(inst.getString());
        }
    }

    public void addInstruction(Instruction inst)
    {
        instructions.add(inst);
    }

}
