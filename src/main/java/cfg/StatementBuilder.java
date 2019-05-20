package cfg;

import ast.exp.Expression;
import ast.prog.StructEntry;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.stmt.*;
import llvm.inst.*;
import llvm.type.*;
import llvm.value.*;

import java.util.ArrayList;
import java.util.List;

public class StatementBuilder {

    boolean stackBased;
    ExpressionBuilder expBuilder;
    TypeConverter converter;
    SymbolTableList symbolTableList;
    StructTable structTable;
    BasicBlock entryNode;
    BasicBlock exitNode;
    ast.type.Type retType;
    List<BasicBlock> nodeList;

    public StatementBuilder(boolean stackBased, TypeConverter converter, SymbolTableList symbolTableList,
                            StructTable structTable, BasicBlock entry, BasicBlock exit, ast.type.Type retType,
                                    List<BasicBlock> nodeList)
    {
        this.stackBased = stackBased;
        this.expBuilder = new ExpressionBuilder(stackBased, converter, symbolTableList, structTable);
        this.converter = converter;
        this.symbolTableList = symbolTableList;
        this.structTable = structTable;
        this.entryNode = entry;
        this.exitNode = exit;
        this.retType = retType;
        this.nodeList = nodeList;
    }

    public BasicBlock build(BlockStatement start, BasicBlock entry)
    {
        return AddBlockStatement(start, entry);
    }

    private BasicBlock AddBlockStatement(BlockStatement block, BasicBlock currentBlock)
    {
        BasicBlock newCurBlock = currentBlock;

        if ((block.getLineNum() == -1) || (block.getStatements().size() == 0))
        {
            // Body is an empty block
            return null;
        }
        for (Statement stmt : block.getStatements())
        {
            newCurBlock = AddStatement(stmt, newCurBlock);
        }
        return newCurBlock;
    }

    private BasicBlock AddAssignmentStatement(AssignmentStatement stmt, BasicBlock currentBlock)
    {
        Lvalue lval = stmt.getTarget();
        Expression source = stmt.getSource();
        Value lvalLoc = AddlVal(lval, currentBlock);
        Value sourceLoc = expBuilder.AddExpression(source, currentBlock);

        if (stackBased || lval instanceof LvalueDot)
        {
            currentBlock.addInstruction(new Store(sourceLoc, lvalLoc));
        }
        else
        {
            //System.out.println("writing: " + lvalLoc.getId() + " to " + currentBlock.label.getString() + " : " + sourceLoc.getString());
            // TODO this is a serious hack lol
            currentBlock.writeVariable(lvalLoc.getId(), sourceLoc);
        }

        return currentBlock;
    }

    private Value AddlVal(Lvalue lVal, BasicBlock currentBlock)
    {
        if (lVal instanceof LvalueDot)
        {
            LvalueDot valDot = (LvalueDot) lVal;
            String id = valDot.getId();
            // get type (struct) of left expression
            Expression lExp = valDot.getLeft();

            // add base address instruction
            Value localLoc = expBuilder.AddExpression(lExp, currentBlock);
            Type sType = localLoc.getType();

            // add offset address instruction
            Value offsetAddr;
            StructEntry entry = structTable.get(((Struct)sType).getName());
            int index = entry.getFieldIndex(id);
            Type fieldType = converter.convertType(entry.getType(id));

            if (stackBased)
            {
                offsetAddr = new StackLocation(fieldType);
            }
            else
            {
                offsetAddr = new Register(fieldType);
            }


            currentBlock.addInstruction(new Getelementptr(offsetAddr, localLoc, index));
            return offsetAddr;
        }
        else
        {
            LvalueId valId = (LvalueId) lVal;
            String id = valId.getId();
            //return getLocalFromId(id);

            if (stackBased)
            {
                return expBuilder.getLocalFromId(id);
            }
            else
            {
                Type type = converter.convertType(symbolTableList.typeOf(id));
                //return currentBlock.readVariable(id, type);
                return new Local(id, type);
            }
        }
    }

    private BasicBlock AddDeleteStmt(DeleteStatement delStmt, BasicBlock currentBlock)
    {
        Value val = expBuilder.AddExpression(delStmt.getExpression(), currentBlock);
        Value result;
        if (stackBased)
        {
            result = new StackLocation(new i8());
        }
        else
        {
            result = new Register(new i8());
        }
        // TODO reuse call? issue: free has no result
        currentBlock.addInstruction(new Bitcast(val, result));
        currentBlock.addInstruction(new Free(result));
        return currentBlock;
    }

    private BasicBlock AddPrintLnStmt(PrintLnStatement pStmt, BasicBlock currentBlock)
    {
        Value expVal = expBuilder.AddExpression(pStmt.getExpression(), currentBlock);
        // TODO implement PrintLn better
        currentBlock.addInstruction(new PrintLn(expVal, new i32()));
        return currentBlock;
    }

    private BasicBlock AddPrintStmt(PrintStatement pStmt, BasicBlock currentBlock)
    {
        Value expVal = expBuilder.AddExpression(pStmt.getExpression(), currentBlock);
        // TODO implement Print better
        currentBlock.addInstruction(new Print(expVal));
        return currentBlock;
    }

    private BasicBlock AddStatement(ast.stmt.Statement stmt, BasicBlock currentBlock)
    {
        if (stmt instanceof AssignmentStatement)
        {
            AssignmentStatement assignStmt = (AssignmentStatement) stmt;

            return AddAssignmentStatement(assignStmt, currentBlock);
        }
        else if (stmt instanceof BlockStatement)
        {
            BlockStatement blockStmt = (BlockStatement) stmt;
            return AddBlockStatement(blockStmt, currentBlock);
        }
        else if (stmt instanceof ConditionalStatement)
        {
            ConditionalStatement condStmt = (ConditionalStatement) stmt;
            return AddConditionalStmt(condStmt, currentBlock);
        }
        else if (stmt instanceof DeleteStatement)
        {
            DeleteStatement delStmt = (DeleteStatement) stmt;
            return AddDeleteStmt(delStmt, currentBlock);
        }
        else if (stmt instanceof InvocationStatement)
        {
            InvocationStatement invStmt = (InvocationStatement) stmt;
            expBuilder.AddExpression(invStmt.getExpression(), currentBlock);
            return currentBlock;
        }
        else if (stmt instanceof PrintLnStatement)
        {
            PrintLnStatement pStmt = (PrintLnStatement) stmt;
            return AddPrintLnStmt(pStmt, currentBlock);
        }
        else if (stmt instanceof PrintStatement)
        {
            PrintStatement pStmt = (PrintStatement) stmt;
            return AddPrintStmt(pStmt, currentBlock);
        }
        else if (stmt instanceof ReturnEmptyStatement)
        {
            // TODO sloppy
            if (!stackBased)
            {
                exitNode.seal();
            }
            return AddEmptyReturnStmt(currentBlock);
        }
        else if (stmt instanceof ReturnStatement)
        {
            // TODO sloppy
            if (!stackBased)
            {
                exitNode.seal();
            }
            ReturnStatement retStmt = (ReturnStatement) stmt;
            return AddReturnStmt(retStmt, currentBlock);
        }
        else if (stmt instanceof  WhileStatement)
        {
            WhileStatement whileStmt = (WhileStatement) stmt;
            return AddWhileStatement(whileStmt, currentBlock);
        }
        return currentBlock;
    }

    private BasicBlock AddEmptyReturnStmt(BasicBlock currentBlock)
    {
        currentBlock.addInstruction(new BrUncond(exitNode.label));
        currentBlock.successorList.add(exitNode);
        exitNode.predecessorList.add(currentBlock);
        exitNode.addInstruction(new ReturnVoid());
        return exitNode;
    }

    private BasicBlock AddReturnStmt(ReturnStatement retStmt, BasicBlock currentBlock)
    {
        Value retExpVal = expBuilder.AddExpression(retStmt.getExpression(), currentBlock);
        Type type = converter.convertType(retType);
        if (stackBased)
        {
            Value retVal = new Local("_retval_", type);
            currentBlock.addInstruction(new Store(retExpVal, retVal));
        }
        else
        {
            currentBlock.writeVariable("_retval_", retExpVal);
        }

        currentBlock.addInstruction(new BrUncond(exitNode.label));
        currentBlock.successorList.add(exitNode);
        exitNode.predecessorList.add(currentBlock);
        return exitNode;
    }

    private BasicBlock AddConditionalStmt(ConditionalStatement stmt, BasicBlock currentBlock)
    {

        // Create predecessor list for entry blocks
        List<BasicBlock> entryPreds = new ArrayList<>();
        entryPreds.add(currentBlock);

        // Create entry blocks
        BasicBlock thenEntry = new BasicBlock(entryPreds, stackBased);
        BasicBlock elseEntry = new BasicBlock(entryPreds, stackBased);

        // Add guard
        Value guardLoc = expBuilder.AddExpression(stmt.getGuard(), currentBlock);
        Value extGuard;
        if (stackBased)
        {
            extGuard = new StackLocation(new i1());
        }
        else
        {
            extGuard = new Register(new i1());
        }
        currentBlock.addInstruction(new Trunc(guardLoc, extGuard));
        currentBlock.addInstruction(new BrCond(extGuard, thenEntry.label, elseEntry.label));

        // Create EXIT block
        List<BasicBlock> exitPreds = new ArrayList<>();
        BasicBlock exitBlock = new BasicBlock(exitPreds, stackBased);

        // Keep track of where to branch and what block to return
        BasicBlock retBlock;

        // Populate THEN entry block
        nodeList.add(thenEntry);
        thenEntry.seal();
        currentBlock.successorList.add(thenEntry);
        BasicBlock thenExit = AddStatement(stmt.getThenBlock(), thenEntry);

        if (thenExit != exitNode)
        {
            if (thenExit != thenEntry)
            {
                thenExit.seal();
            }
            thenExit.successorList.add(exitBlock);
            exitBlock.predecessorList.add(thenExit);

            thenExit.addInstruction(new BrUncond(exitBlock.label));
            nodeList.add(exitBlock);
            retBlock = exitBlock;
        }
        else
        {
            // TODO the battle of Fib vs BenchMarkish
            retBlock = exitBlock;
            nodeList.add(exitBlock);
            //retBlock = exitNode;
        }

        // Populate ELSE entry block
        BasicBlock elseExit = AddStatement(stmt.getElseBlock(), elseEntry);

        elseEntry.seal();
        nodeList.add(elseEntry);
        if (elseEntry.instructions.size() != 0)
        {
            if (elseExit != exitNode)
            {
                if (elseExit != elseEntry)
                {
                    elseExit.seal();
                }
                elseExit.successorList.add(exitBlock);

                exitBlock.predecessorList.add(elseExit);

                elseExit.addInstruction(new BrUncond(exitBlock.label));
                retBlock = exitBlock;
                // TODO is there a case where exitBlock won't be in nodelist?
            }
        }
        else
        {
            exitBlock.predecessorList.add(elseEntry);
            elseEntry.addInstruction(new BrUncond(retBlock.label));
        }

        //exitBlock.predecessorList.add(elseExit); // TODO ?
        //exitBlock.predecessorList.add(thenExit); // TODO ?
        exitBlock.seal();

        return retBlock;
    }

    private BasicBlock AddWhileStatement(WhileStatement stmt, BasicBlock currentBlock)
    {
        // create true block
        List<BasicBlock> predList = new ArrayList<>();
        predList.add(currentBlock);
        BasicBlock trueEntryNode = new BasicBlock(predList, stackBased);

        // create false block
        BasicBlock falseNode = new BasicBlock(predList, stackBased);
        nodeList.add(trueEntryNode);
        nodeList.add(falseNode);

        // Get value of guard and add to current block
        Value guardVal = expBuilder.AddExpression(stmt.getGuard(), currentBlock);
        Value extGuard;
        if (stackBased)
        {
            extGuard = new StackLocation(new i1());
        }
        else
        {
            extGuard = new Register(new i1());
        }
        currentBlock.addInstruction(new Trunc(guardVal, extGuard));
        Instruction brInst = new BrCond(extGuard, trueEntryNode.label, falseNode.label);
        currentBlock.addInstruction(brInst);
        currentBlock.successorList.add(trueEntryNode);
        currentBlock.successorList.add(falseNode);

        Statement trueStmt = stmt.getBody();
        BasicBlock trueExitNode = AddStatement(trueStmt, trueEntryNode);
        falseNode.predecessorList.add(trueExitNode);
        if (!(trueEntryNode.predecessorList.contains(trueExitNode)))
        {
            trueEntryNode.predecessorList.add(trueExitNode); // TODO adding same pred multiple times
        }


        // add guard to end of true block
        Value guardValT = expBuilder.AddExpression(stmt.getGuard(), trueExitNode);
        Value extGuardT;
        if (stackBased)
        {
            extGuardT = new StackLocation(new i1());
        }
        else
        {
            extGuardT = new Register(new i1());
        }
        trueExitNode.addInstruction(new Trunc(guardValT, extGuardT));
        Instruction brInstT = new BrCond(extGuardT, trueEntryNode.label, falseNode.label);
        trueExitNode.addInstruction(brInstT);

        // TODO is this where to seal?
        trueEntryNode.seal();
        trueExitNode.seal();

        falseNode.seal();
        return falseNode;
    }
}
