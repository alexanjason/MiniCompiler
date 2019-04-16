package cfg;

import ast.*;
import ast.exp.*;
import ast.stmt.*;
import ast.type.FunctionType;
import llvm.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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

    // (directed) edges denote flow between blocks

    ControlFlowGraph(ast.Function func, StructTable structTable, SymbolTableList symbolTableList)
    {
        this.structTable = structTable;
        this.symbolTableList = symbolTableList;
        this.function = func;
        entryNode = new BasicBlock(null);
        exitNode = new BasicBlock(null); //TODO
        nodeList = new ArrayList<>();
        nodeList.add(entryNode);
        BuildCFG();

    }

    public void print(PrintStream stream)
    {
        printFunction(stream);
        for (BasicBlock block : nodeList)
        {
            block.print(stream);
        }
    }

    public void printFunction(PrintStream stream)
    {
        Type retType = convertType(function.getRetType());
        stream.print("define " + retType.getString() + "@"
                + function.getName() + "(");
        for (Declaration dec : function.getParams())
        {
            stream.print(convertType(dec.getType()).getString());
            stream.print(" ");
            stream.print("%" + dec.getName());
            stream.print(" ");
        }
        stream.print(")\n");
        stream.print("{\n");
    }

    private void BuildCFG()
    {
        // allocate params
        AddAllocateParamsInst(function.getParams());

        // allocate locals
        AddAllocateLocalsInst(function.getLocals());

        // create instructions from body
        AddBodyInst(function.getBody());
    }

    private void AddAllocateParamsInst(List<Declaration> params)
    {
        for (Declaration dec : params)
        {
            // Create allocation instruction
            Instruction inst = new llvm.Allocate("_P_" + dec.getName(), convertType(dec.getType()));

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);
        }
    }

    private Type convertType(ast.type.Type astType)
    {
        if (astType instanceof ast.type.IntType)
        {
            return new i32();
        }
        else if (astType instanceof ast.type.BoolType)
        {
            return new i32(); // TODO deal with this
        }
        else if (astType instanceof ast.type.StructType)
        {
            ast.type.StructType sType = (ast.type.StructType) astType;
            return new Struct(sType.GetName());
        }
        System.err.println("Undealt with type");
        System.exit(8);
        return null;

    }

    private void AddAllocateLocalsInst(List<Declaration> locals)
    {
        for (Declaration dec : locals)
        {
            // Create allocation instruction
            Instruction inst = new llvm.Allocate(dec.getName(), convertType(dec.getType()));

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);
        }
    }

    private void AddBodyInst(Statement body)
    {
         if (body instanceof BlockStatement)
         {
             BlockStatement block = (BlockStatement) body;
             AddBlockStatement(block, entryNode);
         }
         else
         {
             System.err.println("Function body not a BlockStatement");
         }
    }

    private BasicBlock AddBlockStatement(BlockStatement block, BasicBlock currentBlock)
    {
        BasicBlock newCurBlock = currentBlock;

        if (block.getLineNum() == -1)
        {
            // TODO body is empty block
        }
        for (Statement stmt : block.getStatements())
        {
            newCurBlock = AddStatement(stmt, newCurBlock);
        }
        return newCurBlock;

    }

    private BasicBlock AddStatement(ast.stmt.Statement stmt, BasicBlock currentBlock)
    {
        if (stmt instanceof AssignmentStatement)
        {
            AssignmentStatement assignStmt = (AssignmentStatement) stmt;
            Lvalue lval = assignStmt.getTarget();
            Expression source = assignStmt.getSource();

            Value lvalLoc = AddlVal(lval, currentBlock);
            Value sourceLoc = AddExpression(source, currentBlock);

            // TODO there's got to be a better way
            Type lvalType = convertType(lval.TypeCheck(structTable, symbolTableList));
            Instruction inst = new Store(sourceLoc, lvalType, lvalLoc);

            currentBlock.addInstruction(inst);

            return currentBlock;
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
            // TODO
            return currentBlock;

        }
        else if (stmt instanceof InvocationStatement)
        {
            InvocationStatement invStmt = (InvocationStatement) stmt;
            AddExpression(invStmt.getExpression(), currentBlock);
            // TODO is this right?
            return currentBlock;
        }
        else if (stmt instanceof PrintLnStatement)
        {
            //TODO
            return currentBlock;
        }
        else if (stmt instanceof PrintStatement)
        {
            //TODO
            return currentBlock;
        }
        else if (stmt instanceof ReturnEmptyStatement)
        {
            currentBlock.addInstruction(new BrUncond(exitNode.label));
            currentBlock.successorList.add(exitNode);
            exitNode.predecessorList.add(currentBlock);
            exitNode.addInstruction(new ReturnVoid());
            nodeList.add(exitNode);
            return exitNode;
        }
        else if (stmt instanceof ReturnStatement)
        {
            ReturnStatement retStmt = (ReturnStatement) stmt;
            Value retExpVal = AddExpression(retStmt.getExpression(), currentBlock);
            currentBlock.addInstruction(new BrUncond(exitNode.label));
            currentBlock.successorList.add(exitNode);
            exitNode.predecessorList.add(currentBlock);
            Type retType = convertType(function.getRetType());
            exitNode.addInstruction(new Return(retType, retExpVal));
            nodeList.add(exitNode);
            return exitNode;
        }
        else if (stmt instanceof  WhileStatement)
        {
            WhileStatement whileStmt = (WhileStatement) stmt;
            return AddWhileStatement(whileStmt, currentBlock);
        }
        return currentBlock;
    }

    private BasicBlock AddConditionalStmt(ConditionalStatement stmt, BasicBlock currentBlock)
    {
        // true block
        List<BasicBlock> predList = new ArrayList<>();
        predList.add(currentBlock);
        BasicBlock thenEntryNode = new BasicBlock(predList);
        BasicBlock thenExitNode = AddStatement(stmt.getThenBlock(), thenEntryNode);

        // false block
        BasicBlock elseEntryNode = new BasicBlock(predList);
        BasicBlock elseExitNode = AddStatement(stmt.getElseBlock(), elseEntryNode);

        // add guard to end of current block
        Value guardLoc = AddExpression(stmt.getGuard(), currentBlock);
        currentBlock.addInstruction(new BrCond(guardLoc, thenEntryNode.label, elseEntryNode.label));

        // create exit block
        List<BasicBlock> predCondList = new ArrayList<>();
        predCondList.add(thenExitNode);
        predCondList.add(elseExitNode);
        BasicBlock exitNode = new BasicBlock(predCondList);

        // join blocks
        nodeList.add(thenEntryNode);
        nodeList.add(elseEntryNode);
        if (thenEntryNode != thenExitNode)
        {
            // TODO does this work??
            nodeList.add(thenExitNode);
        }
        if (elseEntryNode != elseExitNode)
        {
            // TODO does this work??
            nodeList.add(elseExitNode);
        }
        nodeList.add(exitNode);

        thenExitNode.successorList.add(exitNode);
        elseExitNode.successorList.add(exitNode);

        return exitNode;

    }

    private BasicBlock AddWhileStatement(WhileStatement stmt, BasicBlock currentBlock)
    {
        // create true block
        List<BasicBlock> predList = new ArrayList<>();
        predList.add(currentBlock);
        BasicBlock trueEntryNode = new BasicBlock(predList);

        Statement trueStmt = stmt.getBody();
        BasicBlock trueExitNode = AddStatement(trueStmt, trueEntryNode);

        // create false block
        List<BasicBlock> exitPredList = new ArrayList<>();
        exitPredList.add(trueExitNode);
        exitPredList.add(currentBlock);
        BasicBlock falseNode = new BasicBlock(exitPredList);
        nodeList.add(trueEntryNode);
        if (trueEntryNode != trueExitNode)
        {
            // TODO does this work?
            nodeList.add(trueExitNode);
        }
        nodeList.add(falseNode);

        // Get value of guard and add to current block
        Value guardVal = AddExpression(stmt.getGuard(), currentBlock);
        Instruction brInst = new BrCond(guardVal, trueEntryNode.label, falseNode.label);
        currentBlock.addInstruction(brInst);
        currentBlock.successorList.add(trueEntryNode);
        currentBlock.successorList.add(falseNode);

        // add guard to end of true block
        trueExitNode.addInstruction(brInst);

        return falseNode;
    }

    private Value AddInvocationExpression(InvocationExpression exp, BasicBlock currentBlock)
    {
        List<Type> paramTypes = new ArrayList<>();
        List<Value> paramVals = new ArrayList<>();
        String fName = exp.getName();
        FunctionType fType = (FunctionType) symbolTableList.typeOf(fName);
        Type retType = convertType(fType.getReturnType());

        int i = 0;
        for (Expression pExp : exp.getArguments())
        {
            paramVals.add(AddExpression(pExp, currentBlock));
            paramTypes.add(convertType(fType.getParamType(i)));
            i++;
        }
        Value result = new StackLocation();
        Instruction callInst = new Call(result, retType, fName, paramTypes, paramVals);
        currentBlock.addInstruction(callInst);
        return result;
    }

    private Value AddBinaryExpression(BinaryExpression exp, BasicBlock currentBlock)
    {
        BinaryExpression.Operator op = exp.getOperator();
        Value leftLoc = AddExpression(exp.getLeft(), currentBlock);
        Value rightLoc = AddExpression(exp.getRight(), currentBlock);
        Value result = new StackLocation();
        if (op == BinaryExpression.Operator.TIMES)
        {
            currentBlock.addInstruction(new Mult(result, leftLoc, rightLoc, new i32()));
            return result;
        }
        else if (op == BinaryExpression.Operator.DIVIDE)
        {
            currentBlock.addInstruction(new Sdiv(result, leftLoc, rightLoc, new i32()));
            return result;
        }
        else if (op == BinaryExpression.Operator.PLUS)
        {
            currentBlock.addInstruction(new Add(result, leftLoc, rightLoc, new i32()));
            return result;
        }
        else if (op == BinaryExpression.Operator.MINUS)
        {
            currentBlock.addInstruction(new Sub(result, leftLoc, rightLoc, new i32()));
            return result;
        }
        else if (op == BinaryExpression.Operator.LT)
        {
            currentBlock.addInstruction(new Icmp(result, "slt", new i32(), leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.GT)
        {
            currentBlock.addInstruction(new Icmp(result, "sgt", new i32(), leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.LE)
        {
            currentBlock.addInstruction(new Icmp(result, "sle", new i32(), leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.GE)
        {
            currentBlock.addInstruction(new Icmp(result, "sge", new i32(), leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.EQ)
        {
            currentBlock.addInstruction(new Icmp(result, "eq", new i32(), leftLoc, rightLoc));
            // TODO could be struct
            return result;
        }
        else if (op == BinaryExpression.Operator.NE)
        {
            currentBlock.addInstruction(new Icmp(result, "ne", new i32(), leftLoc, rightLoc));
            // TODO could be struct
            return result;
        }
        else if (op == BinaryExpression.Operator.AND)
        {
            // TODO bool type which is just i32
        }
        else if (op == BinaryExpression.Operator.OR)
        {
            // TODO bool type which is just i32
        }
        return null; // TODO remove
    }


    private Value AddDotExpression(DotExpression exp, BasicBlock currentBlock)
    {
        Value leftLoc = AddExpression(exp.getLeft(), currentBlock);
        Value result = new StackLocation();

        currentBlock.addInstruction(new GetElementPtr(result, null, null, new i32(), 0, new i32(), null));
        //TODO: determine what to pass for typePtr, ptrVal, index  ^
        //TODO: Determine initType and indexType - currently just assuming i32

        return result;

    }

    private Value AddExpression(ast.exp.Expression exp, BasicBlock currentBlock)
    {
        // TODO could expressions yield multiple instructions?

        if (exp instanceof BinaryExpression)
        {
            BinaryExpression bExp = (BinaryExpression) exp;
            return AddBinaryExpression(bExp, currentBlock);
        }
        else if (exp instanceof DotExpression)
        {
            DotExpression dotExp = (DotExpression) exp;
            return AddDotExpression(dotExp, currentBlock);
        }
        else if (exp instanceof FalseExpression)
        {
            return new Immediate("false");
        }
        else if (exp instanceof IdentifierExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof IntegerExpression)
        {
            return new Immediate(((IntegerExpression) exp).getValue());
        }
        else if (exp instanceof InvocationExpression)
        {
            InvocationExpression invExp = (InvocationExpression) exp;
            return AddInvocationExpression(invExp, currentBlock);
        }
        else if (exp instanceof NewExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof NullExpression)
        {
            return new Immediate("null");
        }
        else if (exp instanceof ReadExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof TrueExpression)
        {
            return new Immediate("true");
        }
        else if (exp instanceof UnaryExpression)
        {
            // TODO
            return null;
        }
        else
        {
            System.out.println("No expression pattern matched");
            System.exit(8);
            return null;
        }
    }

    private Value AddlVal(ast.Lvalue lVal, BasicBlock currentBlock)
    {
        // TODO
        return null;
    }

}
