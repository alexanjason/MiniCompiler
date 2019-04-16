package cfg;

import ast.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ControlFlowGraphList {

    protected List<ControlFlowGraph> controlFlowGraphs;

    protected Program program;

    public ControlFlowGraphList(Program program)
    {
        this.program = program;
        controlFlowGraphs = new ArrayList<>();
        for (Function func : program.getFuncs())
        {
            controlFlowGraphs.add(new ControlFlowGraph(func, program.getStructTable(), program.getSymbolTables()));
        }
    }

    public void print(PrintStream stream)
    {
        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            cfg.print(stream);
        }
    }

}
