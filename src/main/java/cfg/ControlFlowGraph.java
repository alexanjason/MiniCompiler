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

    protected List<Value> values;

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
        this.values = new ArrayList<>();
        entryNode = new BasicBlock(new ArrayList<>(), stackBased, values);
        exitNode = new BasicBlock(new ArrayList<>(), stackBased, values);
        entryNode.seal();

        Type type = converter.convertType(function.getRetType());

        // TODO put this retval business in a class? Value?

        entryNode.addInstruction(new FuncStart());
        entryNode.addInstruction(new ParamMov(function.getParamNames()));

        if (!(type instanceof Void))
        {
            if (stackBased)
            {
                Value result = new StackLocation(type);
                values.add(result);
                Value retVal = new Local("_retval_", type);
                values.add(retVal);
                entryNode.addInstruction(new Allocate("_retval_", type));
                exitNode.addInstruction(new Load(result, retVal));
                exitNode.addInstruction(new Return(result));
            }
        }

        nodeList = new ArrayList<>();
        nodeList.add(entryNode);
        BuildCFG();
    }

    public void localValueNumbering()
    {
        for (BasicBlock b : nodeList)
        {
            b.localValueNumbering();
        }
    }

    public void uselessCodeElimination()
    {
        boolean guard = true;
        while (guard)
        {
            guard = false;
            for (Value v : values)
            {
                v.checkUseless();
            }

            for (BasicBlock b : nodeList)
            {
                if (b.uselessCodeElimination())
                {
                    guard = true;
                }
            }
        }
    }

    public void constantPropagation()
    {
        Map<Value, SSCPValue> sscpMap = new HashMap<>();
        List<Value> workList = new ArrayList<>();

        // initialize value mappings
        for (Value v : values)
        {
            // TODO hacky
            if (v instanceof Global)
            {
                System.err.println(v.getString() + " global");
                sscpMap.put(v, new SSCPValue.Bottom());
                workList.add(v);
            }
            else if (v instanceof Local)
            {
                System.err.println(v.getString() + " local");
                if (((Local)v).isParam())
                {
                    System.err.println(v.getString() + " param");
                    // TODO so much duplicate code in llvm instructions
                    sscpMap.put(v, new SSCPValue.Bottom());
                    workList.add(v);
                }
                else
                {
                    // TODO hacky and possibly incorrect
                    /*
                    if (v.getDef() == null)
                    {
                        sscpMap.put(v, new SSCPValue.Top());
                    }
                    else
                    {
                        System.err.println("ELSE NEEDS HELP");
                        System.out.println(v.getString());
                        v.getDef().print(System.out);
                    }
                    */
                }
            }
            else
            {
                // TODO quick mindless hack
                if (v.getDef() == null)
                {
                    System.err.println(v.getString() + " def == null");
                }
                else
                {
                    v.getDef().sscpInit(sscpMap, workList);
                }
            }
            /*
            System.out.println(v.getString() + " vvv ");

            for (Value vs : sscpMap.keySet())
            {
                System.out.println(vs.getString() + " -> " + sscpMap.get(vs).getString());
            }
            */
        }

        //Iterator iterator = workList.iterator();
        while (workList.size() != 0) {

            System.err.print("WORKLIST: ");
            for (Value v : workList)
            {
                System.out.print(v.getString() + " ");
            }
            System.out.println();

            ListIterator<Value> iterator = workList.listIterator();

            while (iterator.hasNext()) {
                Value r = iterator.next();
                //System.err.println("removing " + r.getString() + " from worklist");
                iterator.remove();


                for (Instruction inst : r.getUses()) {
                    //System.err.println("use: " + inst.getString());
                    inst.sscpEval(sscpMap, iterator);
                }
            }
        }

        System.out.println("SSCP MAPPINGS: ");
        for (Value v : sscpMap.keySet())
        {
            System.out.println(v.getString() + " -> " + sscpMap.get(v).getString());
        }

        for (Value v : sscpMap.keySet())
        {
            if (sscpMap.get(v) instanceof SSCPValue.Constant)
            {
                Object c = ((SSCPValue.Constant)sscpMap.get(v)).getConst();
                Immediate constant;
                if (c instanceof Boolean)
                {
                    constant = new Immediate(Boolean.toString((Boolean)c), new i1());
                }
                else if (c instanceof Integer)
                {
                    constant = new Immediate(Integer.toString((Integer)c), new i32());
                }
                else if (c == null)
                {
                     constant = new Immediate("null", new i32());
                }
                else
                {
                    constant = null;
                    System.err.println("PANIC sscp constant to immediate " + c.toString());
                }
                //ListIterator<Instruction> iterator = v.getUses().listIterator();
                //while (iterator.hasNext())
                List<Instruction> list = new ArrayList<>(v.getUses());
                for (Instruction inst : list)
                {
                    inst.sscpReplace(v, constant);
                    //Instruction inst = iterator.next();
                    //inst.sscpReplace(v, constant, iterator);
                }
            }
        }
    }

    public void propagateLiveOutSets()
    {
        boolean guard = true;

        while(guard)
        {
            guard = false;
            // TODO move to BasicBlock?
            for (BasicBlock n : nodeList)
            {
                Set<Value> newLiveOut = new HashSet<>();
                //System.err.println("Block n: " + n.label.getString());
                for (BasicBlock m : n.successorList)
                {
                    //System.err.println("Block m: " + m.label.getString());
                    Set<Value> temp = new HashSet<>();//m.liveOut.clone(); // TODO clone
                    temp.addAll(m.liveOut);
                    Set<Value> all = m.genSet;
                    System.err.println(m.genSet);

                    // LiveOut(m) - Kill(m)
                    temp.removeAll(m.killSet);

                    // Gen(m) U (LiveOut(m) - Kill(m))
                    temp.addAll(all);
                    //all.addAll(temp);

                    // LiveOut(n) = Union all m : Gen(m) U (LiveOut(m) - Kill(m))
                    newLiveOut.addAll(temp);

                }

                if (!(n.liveOut.equals(newLiveOut)))
                {
                    guard = true;
                }
                n.liveOut = newLiveOut;
            }
        }


        /*
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
        */

    }

    public InterferenceGraph buildInterferenceGraph()
    {
        InterferenceGraph graph = new InterferenceGraph();

        for (BasicBlock b : nodeList)
        {
            System.out.println("**Label " + b.label.getString());
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

        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public void regAlloc()
    {
        propagateLiveOutSets();

        InterferenceGraph interferenceGraph = buildInterferenceGraph();
        interferenceGraph.regAlloc();

        //interferenceGraph.printGraph();
        //interferenceGraph.printMap();
        System.out.println(function.getName() + " NUM SPILLS: " + interferenceGraph.spillCount);
        FuncStart f = (FuncStart) entryNode.instructions.get(0);
        f.updateSpills(interferenceGraph.spillCount);

        ListIterator<arm.Instruction> armInsts = f.getArm().listIterator();
        while (armInsts.hasNext())
        {
            armInsts.next();
        }
        while (armInsts.hasPrevious())
        {
            arm.Instruction inst = armInsts.previous();
            entryNode.addArmInstructionToBeginning(inst);
        }


        System.err.println("spill map");
        for (String s : interferenceGraph.spillMap.keySet())
        {
            System.out.println(s + " -> " + interferenceGraph.spillMap.get(s)*4);
        }
        //System.out.println("exit block " + exitNode.label.getString());
        //FuncEnd e;
        //FuncEnd e = (FuncEnd) exitNode.instructions.get(exitNode.instructions.size()-2);
        for (Instruction i : exitNode.instructions)
        {
            if (i instanceof FuncEnd)
            {
                FuncEnd e = (FuncEnd) i;
                e.updateSpills(interferenceGraph.spillCount);
                ListIterator<arm.Instruction> armEInsts = e.getArm().listIterator();
                while (armEInsts.hasNext())
                {
                    exitNode.addArmInstruction(armEInsts.next());
                }
            }
        }


        for (BasicBlock b : nodeList)
        {

            b.replaceRegs(interferenceGraph.regMappings, interferenceGraph.spillMap);
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
            AddParamsToMapping(function.getParams());
        }

        // create instructions from body
        AddBodyInst(function.getBody());
    }

    private void AddParamsToMapping(List<Declaration> params)
    {
        for (Declaration dec : params)
        {
            Type type = converter.convertType(dec.getType());
            String name = dec.getName();
            // TODO have Paramerer class instead of hacky constructor
            Value localParam = new Local(name, type, true);
            values.add(localParam);
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

            // TODO have Paramerer class instead of hacky constructor
            Value localParam = new Local(param, type, true);
            values.add(localParam);
            Instruction inst = new Allocate(param, type);

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);

            // load param into allocated location
            Value actualParam = new Local(dec.getName(), type);
            values.add(actualParam);
            entryNode.addInstruction(new Store(actualParam, localParam));
        }
    }

    private void AddBodyInst(Statement body)
    {
        if (body instanceof BlockStatement)
        {
            BlockStatement block = (BlockStatement) body;
            StatementBuilder builder = new StatementBuilder(stackBased, converter,
                    symbolTableList, structTable, entryNode, exitNode, function.getRetType(), nodeList, values);
            BasicBlock lastBlock = builder.build(block, entryNode);

            Type type = converter.convertType(function.getRetType());
            if (lastBlock == null)
            {
                entryNode.addInstruction(new BrUncond(exitNode.label));
                entryNode.successorList.add(exitNode);
            }

            if (type instanceof Void)
            {
                exitNode.addInstruction(new FuncEnd(function.getName()));

                // TODO mindless hack
                if (lastBlock != null)
                {
                    lastBlock.addInstruction(new BrUncond(exitNode.label));
                    lastBlock.successorList.add(exitNode);
                }
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
