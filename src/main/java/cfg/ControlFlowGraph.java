package cfg;

import ast.prog.*;
import ast.stmt.*;
import llvm.inst.*;
import llvm.type.*;
import llvm.type.Void;
import llvm.value.*;

import java.io.PrintStream;
import java.util.*;

public class ControlFlowGraph {

    // one graph per function

    // start with a single entry node
    protected BasicBlock entryNode;

    // terminate with single exit node
    protected BasicBlock exitNode;

    // nodes are basic blocks
    protected List<BasicBlock> nodeList;

    protected StructTable structTable;

    protected SymbolTableList symbolTableList;

    protected Function function;

    private static boolean stackBased;

    public static TypeConverter converter;

    // (directed) edges denote flow between blocks

    ControlFlowGraph(Function func, StructTable structTable, SymbolTableList symbolTableList, boolean stack)
    {
        this.structTable = structTable;
        this.symbolTableList = symbolTableList;
        this.function = func;
        this.stackBased = stack;
        this.converter = new TypeConverter(structTable);
        entryNode = new BasicBlock(new ArrayList<>(), stackBased);
        exitNode = new BasicBlock(new ArrayList<>(), stackBased);
        entryNode.seal();

        Type type = converter.convertType(function.getRetType());

        // TODO put this retval business in a class? Value?

        entryNode.addInstruction(new FuncStart(function.getParamNames()));

        if (!(type instanceof Void))
        {
            if (stackBased)
            {
                Value result = new StackLocation(type);
                Value retVal = new Local("_retval_", type);
                entryNode.addInstruction(new Allocate("_retval_", type));
                exitNode.addInstruction(new Load(result, retVal));
                exitNode.addInstruction(new Return(result));
            }
        }

        nodeList = new ArrayList<>();
        nodeList.add(entryNode);
        BuildCFG();
    }

    public void propagateLiveOutSets()
    {
        boolean guard = true;
        //List<BasicBlock> newNodeList = nodeList;
        int count = 0;

        while(guard)
        //Iterator iterator = newNodeList.iterator();
        //while (iterator.hasNext())
        {
            // TODO move to BasicBlock?

            //for (int i = nodeList.size() - 1; i >= 0; i--)
            for (BasicBlock n : nodeList)
            {
                //BasicBlock n = (BasicBlock) iterator.next();
                //System.out.println("***" + n.label.getString() + "***");
                Set<Value> newLiveOut = new HashSet<>();
                //BasicBlock n = nodeList.get(i);

                for (BasicBlock m : n.successorList)
                {
                    //System.out.println(m.label.getString());

                    Set<Value> temp = m.liveOut;
                    //System.out.println("m.liveOut " + temp);
                    Set<Value> all = m.genSet;
                    //System.out.println("m.genSet " + all);

                    // LiveOut(m) - Kill(m)
                    temp.removeAll(m.killSet);

                    // Gen(m) U (LiveOut(m) - Kill(m))
                    all.addAll(temp);

                    // LiveOut(n) = Union all m : Gen(m) U (LiveOut(m) - Kill(m))
                    newLiveOut.addAll(all);
                }

                //System.out.println("liveOut " + n.liveOut);
                //System.out.println("newLiveOut " + newLiveOut);

                if (n.liveOut.equals(newLiveOut))
                {
                    //newNodeList.remove()
                    if (count != 0)
                    {
                        guard = false;
                    }
                    //iterator.remove();
                }
                n.liveOut = newLiveOut;
                count ++;
                //n.liveOut.addAll(newLiveOut);
            }
        }


        for (BasicBlock n : nodeList)
        {
            System.out.println(n.label.getString() + ":");
            System.out.print("\tliveOut: ");
            for (Value v : n.liveOut)
            {
                System.out.print(v.getString() + " ");
            }
            System.out.println();

        }
    }

    public InterferenceGraph buildInterferenceGraph()
    {
        InterferenceGraph graph = new InterferenceGraph();
        for (BasicBlock b : nodeList)
        {
            b.addToInterferenceGraph(graph);
        }
        return graph;
    }

    public void codeGen()
    {
        for (BasicBlock b : nodeList)
        {
            b.propagatePhis();
        }
        for (BasicBlock b : nodeList)
        {
            b.firstPass();
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public void regAlloc()
    {
        propagateLiveOutSets();

        InterferenceGraph interferenceGraph = buildInterferenceGraph();
        interferenceGraph.regAlloc();

        //interferenceGraph.printGraph();
        interferenceGraph.printMap();

        for (BasicBlock b : nodeList)
        {
            b.replaceRegs(interferenceGraph.regMappings, interferenceGraph.spillSet);
        }

    }

    private void BuildCFG()
    {
        if (stackBased)
        {
            // allocate params
            AddAllocateParamsInst(function.getParams());
            // allocate locals
            AddAllocateLocalsInst(function.getLocals());
        }
        else
        {
            // add params to mapping
            // TODO are parameters put into registers??
            AddParamsToMapping(function.getParams());

            // add locals to mapping
            // TODO nothing to do here?
        }

        // create instructions from body
        AddBodyInst(function.getBody());
        //StatementBuilder builder = new StatementBuilder(function.getBody(), entryNode, exitNode);
    }

    private void AddParamsToMapping(List<Declaration> params)
    {
        for (Declaration dec : params)
        {
            Type type = converter.convertType(dec.getType());
            String name = dec.getName();
            Value localParam = new Local(name, type); // TODO local?
            entryNode.writeVariable(name, localParam);
        }
    }

    private void AddAllocateParamsInst(List<Declaration> params)
    {
        for (Declaration dec : params)
        {
            // Create allocation instruction
            Type type = converter.convertType(dec.getType());
            String param = "_P_" + dec.getName();
            Value localParam = new Local(param, type);
            Instruction inst = new Allocate(param, type);

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);

            // load param into allocated location
            Value actualParam = new Local(dec.getName(), type);
            entryNode.addInstruction(new Store(actualParam, localParam));
        }
    }

    private void AddBodyInst(Statement body)
    {
        if (body instanceof BlockStatement)
        {
            BlockStatement block = (BlockStatement) body;
            StatementBuilder builder = new StatementBuilder(stackBased, converter,
                    symbolTableList, structTable, entryNode, exitNode, function.getRetType(), nodeList);
            BasicBlock lastBlock = builder.build(block, entryNode);

            if (lastBlock == null)
            {
                entryNode.addInstruction(new BrUncond(exitNode.label));
            }

            Type type = converter.convertType(function.getRetType());

            if (type instanceof Void)
            {
                exitNode.addInstruction(new FuncEnd(function.getName()));
                lastBlock.addInstruction(new BrUncond(exitNode.label));
                exitNode.addInstruction(new ReturnVoid());
            }

            nodeList.add(exitNode);

            if (!stackBased && !(type instanceof Void))
            {
                Value ret = exitNode.readVariable("_retval_", type);
                exitNode.addInstruction(new Return(ret));
                exitNode.addInstruction(new FuncEnd(function.getName()));
                exitNode.seal();
            }
        }
        else
        {
            System.err.println("Function body not a BlockStatement");
            System.exit(-2);
        }
    }

    private void AddAllocateLocalsInst(List<Declaration> locals) {
        for (Declaration dec : locals) {
            // Create allocation instruction
            Instruction inst = new Allocate(dec.getName(), converter.convertType(dec.getType()));

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);
        }
    }

    public void print(PrintStream stream, boolean llvm)
    {
        printFunction(stream, llvm);
        for (BasicBlock block : nodeList)
        {
            block.print(stream, llvm);
        }
        if (llvm)
        {
            stream.print("}\n");
        }
    }

    public void printFunction(PrintStream stream, boolean llvm)
    {
        if (llvm)
        {
            Type retType = converter.convertType(function.getRetType());
            stream.print("define " + retType.getString() + " @"
                    + function.getName() + "(");
            int i = 0;
            int size = function.getParams().size();
            for (Declaration dec : function.getParams()) {
                stream.print(converter.convertType(dec.getType()).getString());
                stream.print(" ");
                stream.print("%" + dec.getName());
                if (i != size - 1) {
                    stream.print(", ");
                }
                i++;
            }
            stream.print(")\n");
            stream.print("{\n");
        }
        else
        {
            stream.println("\t.align 2");
            stream.println("\t.global " + function.getName());
            stream.println(function.getName() + ":");
        }
    }
}
