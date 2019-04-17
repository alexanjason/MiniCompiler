package cfg;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ast.prog.*;
import llvm.inst.Print;
import llvm.type.Type;

public class ControlFlowGraphList {

    protected List<ControlFlowGraph> controlFlowGraphs;

    protected Program program;

    public ControlFlowGraphList(Program program)
    {
        this.program = program;
        controlFlowGraphs = new ArrayList<>();
        SymbolTableList symbolTables = program.getSymbolTables();
        for (Function func : program.getFuncs())
        {
            // TODO make symbol table a hash table mapping
            //  functions to their symbol table instead of list
            symbolTables.newScope();
            symbolTables.addDecls(func.getParams(), Scope.PARAM);
            symbolTables.addDecls(func.getLocals(), Scope.LOCAL);

            controlFlowGraphs.add(new ControlFlowGraph(func, program.getStructTable(), program.getSymbolTables()));
            symbolTables.removeScope();
        }
    }

    private void printHelpers(PrintStream stream)
    {
        // TODO should this be more than just a string dump?
        StringBuilder sb = new StringBuilder();
        sb.append("declare i8* @malloc(i32)\n");
        sb.append("declare void @free(i8*)\n");
        sb.append("declare i32 @printf(i8*, ...)\n");
        sb.append("declare i32 @scanf(i8*, ...)\n");
        sb.append("@.println = private unnamed_addr constant [5 x i8] c\"%ld" + '\\' + "0A" + '\\' + "00\", align 1\n");
        sb.append("@.print = private unnamed_addr constant [5 x i8] c\"%ld" + '\\' + "00\", align 1\n");
        sb.append("@.read = private unnamed_addr constant [4 x i8] c\"%ld" + '\\' + "00\", align 1\n");
        //sb.append("@.read_scratch = common global i32 0, align 8\n");

        stream.print(sb.toString());
    }

    public void print(PrintStream stream)
    {
        printTypes(program.getTypes(), stream);
        stream.println();
        printDecls(program.getDecls(), stream);

        for (ControlFlowGraph cfg : controlFlowGraphs)
        {
            stream.println();
            cfg.print(stream);
        }
        stream.print("\n");
        printHelpers(stream);
    }

    public void printTypes(List<TypeDeclaration> decls, PrintStream stream)
    {
        // %struct.foo = type {i32, i32, %struct.simple*}
        for (TypeDeclaration tdec : decls)
        {
            stream.print("%struct." + tdec.getName() + " = type {");
            int i = 0;
            int size = tdec.getFields().size();
            for (Declaration dec : tdec.getFields())
            {
                // TODO how i access convertType here is very strange
                stream.print(controlFlowGraphs.get(0).convertType(dec.getType()).getString());
                if (i != size - 1)
                {
                    stream.print(", ");
                }
                i++;
            }
            stream.print("}\n");
        }
    }

    public void printDecls (List<Declaration> decls, PrintStream stream)
    {
        // @globalfoo = common global %struct.foo* null, align 8
        for (Declaration dec : decls)
        {
            Type type = controlFlowGraphs.get(0).convertType(dec.getType());
            stream.println("@" + dec.getName() + " = common global " + type.getString() + " " + type.getDefault() + ", align 8");
        }
    }

}
