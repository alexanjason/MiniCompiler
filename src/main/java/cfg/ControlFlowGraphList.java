package cfg;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ast.prog.*;
import llvm.type.Type;

public class ControlFlowGraphList {

    protected List<ControlFlowGraph> controlFlowGraphs;

    protected Program program;
    private boolean stackBased;

    public ControlFlowGraphList(Program program, boolean stack)
    {
        this.program = program;
        this.stackBased = stack;
        controlFlowGraphs = new ArrayList<>();
        SymbolTableList symbolTables = program.getSymbolTables();
        for (Function func : program.getFuncs())
        {
            // TODO make symbol table a hash table mapping
            //  functions to their symbol table instead of list
            symbolTables.newScope();
            symbolTables.addDecls(func.getParams(), Scope.PARAM);
            symbolTables.addDecls(func.getLocals(), Scope.LOCAL);

            controlFlowGraphs.add(new ControlFlowGraph(func, program.getStructTable(), program.getSymbolTables(), stackBased));
            symbolTables.removeScope();
        }
    }

    public void uselessCodeElimination()
    {
        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            cfg.uselessCodeElimination();
        }
    }

    public void sparseSimpleConstantPropagation()
    {
        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            cfg.constantPropagation();
        }
    }

    public void codeGen()
    {
        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            cfg.codeGen();
        }
    }

    public void regAlloc()
    {
        if (!stackBased)
        {
            for (ControlFlowGraph cfg : controlFlowGraphs)
            {
                cfg.regAlloc();
            }
        }
    }

    public void localValueNumbering()
    {
        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            cfg.localValueNumbering();
        }
    }

    private void printHelpers(PrintStream stream)
    {
        // TODO should this be more than just a string dump?
        StringBuilder sb = new StringBuilder();
        sb.append("declare i8* @malloc(i32)\n");
        sb.append("declare void @free(i8*)\n");
        sb.append("declare i32 @printf(i8*, ...)\n");
        sb.append("@.println = private unnamed_addr constant [5 x i8] c\"%ld" + '\\' + "0A" + '\\' + "00\", align 1\n");
        sb.append("@.print = private unnamed_addr constant [5 x i8] c\"%ld " + '\\' + "00\", align 1\n");
        sb.append("declare i32 @read_util()\n");

        stream.print(sb.toString());
    }

    private void printArmHelpers(PrintStream stream)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(".PRINTLN_FMT:\n");
        sb.append("\t.asciz\t\"%ld\\n\"\n");
        sb.append("\t.align\t2\n");
        sb.append(".PRINT_FMT:\n");
        sb.append("\t.asciz\t\"%ld \"\n");
        sb.append("\t.align\t2\n");
        sb.append(".READ_FMT:\n");
        sb.append("\t.asciz\t\"%ld\"\n");
        sb.append("\t.comm\t.read_scratch,4,4\n");
        sb.append("\t.global\t__aeabi_idiv\n");

        stream.print(sb.toString());
    }

    public void print(PrintStream stream, boolean llvm)
    {
        printHeader(stream, llvm);
        if (llvm)
        {
            printTypes(program.getTypes(), stream);
            stream.println();
        }

        printDecls(program.getDecls(), stream, llvm);

        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            stream.println();
            cfg.print(stream, llvm);
        }

        stream.print("\n");
        if (llvm)
        {
            printHelpers(stream);
        }
        else
        {
            printArmHelpers(stream);
        }
    }

    public void printHeader(PrintStream stream, boolean llvm)
    {
        if (llvm)
        {
            stream.println("target triple=\"i686\"");
        }
        else
        {
            stream.println("\t.arch armv7-a");
            stream.println("\t.text");
        }
    }

    public void printTypes(List<TypeDeclaration> decls, PrintStream stream)
    {
        for (TypeDeclaration tdec : decls)
        {
            stream.print("%struct." + tdec.getName() + " = type {");
            int i = 0;
            int size = tdec.getFields().size();
            for (Declaration dec : tdec.getFields())
            {
                // TODO how i access convertType here is very strange
                stream.print(controlFlowGraphs.get(0).converter.convertType(dec.getType()).getString());
                if (i != size - 1)
                {
                    stream.print(", ");
                }
                i++;
            }
            stream.print("}\n");
        }
    }

    public void printDecls (List<Declaration> decls, PrintStream stream, boolean llvm)
    {
        for (Declaration dec : decls)
        {
            if (llvm)
            {
                Type type = controlFlowGraphs.get(0).converter.convertType(dec.getType());
                stream.println("@" + dec.getName() + " = common global " + type.getString() + " " + type.getDefault() + ", align 4");
            }
            else
            {
                stream.println("\t.comm " + dec.getName() + ",4,4");
            }
        }
    }

}
