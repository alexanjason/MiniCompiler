package cfg;

import ast.exp.*;
import ast.prog.*;
import ast.stmt.*;
import ast.type.FunctionType;
import ast.type.StructType;
import llvm.inst.*;
import llvm.type.Struct;
import llvm.type.Type;
import llvm.type.Void;
import llvm.type.i32;
import llvm.value.Immediate;
import llvm.value.Local;
import llvm.value.StackLocation;
import llvm.value.Value;

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

    ControlFlowGraph(Function func, StructTable structTable, SymbolTableList symbolTableList)
    {
        this.structTable = structTable;
        this.symbolTableList = symbolTableList;
        this.function = func;
        entryNode = new BasicBlock(new ArrayList<>());
        exitNode = new BasicBlock(new ArrayList<>());
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
        stream.print("define " + retType.getString() + " @"
                + function.getName() + "(");
        int i = 0;
        int size = function.getParams().size();
        for (Declaration dec : function.getParams())
        {
            stream.print(convertType(dec.getType()).getString());
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
            Type type = convertType(dec.getType());
            Instruction inst = new Allocate("_P_" + dec.getName(), type);

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);
        }
    }

    public Type convertType(ast.type.Type astType)
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
        else if (astType instanceof ast.type.VoidType)
        {
            return new Void();
        }
        System.out.println(astType);
        System.err.println("Undealt with type");
        System.exit(8);
        return null;

    }

    private void AddAllocateLocalsInst(List<Declaration> locals)
    {
        for (Declaration dec : locals)
        {
            // Create allocation instruction
            Instruction inst = new Allocate(dec.getName(), convertType(dec.getType()));

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

    private Value AddlVal(Lvalue lVal, BasicBlock currentBlock)
    {
        // TODO
        /*
           LvalueDot
           private final int lineNum;
           private final Expression left;
           private final String id;

           LvalueId

           private final int lineNum;
           private final String id;
         */
        if (lVal instanceof LvalueDot)
        {
            LvalueDot valDot = (LvalueDot) lVal;
            Expression lExp = valDot.getLeft();
            String id = valDot.getId();

            // get type (struct) of left expression
            ast.type.Type lType = lExp.TypeCheck(structTable, symbolTableList);
            StructType structType = (StructType) lType;
            Type sType = convertType(lType);

            // add base address instruction
            Value baseAddr = new StackLocation();
            Value localLoc = AddExpression(lExp, currentBlock);
            currentBlock.addInstruction(new Load(sType, baseAddr, localLoc));

            // add offset address instruction
            Value offsetAddr = new StackLocation();
            StructEntry entry = structTable.get(structType.GetName());
            int index = entry.getFieldIndex(id);
            currentBlock.addInstruction(new Getelementptr(offsetAddr, sType, baseAddr, index));

            // add dot instruction
            Value result = new StackLocation();
            Type fieldType = convertType(entry.getType(id));
            currentBlock.addInstruction(new Load(fieldType, result, offsetAddr));
            return result;

        }
        else if (lVal instanceof LvalueId)
        {
            LvalueId valId = (LvalueId) lVal;
            String id = valId.getId();
            Value result = new StackLocation();
            Type type = convertType(symbolTableList.typeOf(id));
            currentBlock.addInstruction(new Load(type, result, new Local(id)));
            return result;
        }

        return null;
    }

    private BasicBlock AddAssignmentStatement(AssignmentStatement stmt, BasicBlock currentBlock)
    {
        Lvalue lval = stmt.getTarget();
        Expression source = stmt.getSource();

        Value lvalLoc = AddlVal(lval, currentBlock);
        Value sourceLoc = AddExpression(source, currentBlock);

        // TODO there's got to be a better way
        Type lvalType = convertType(lval.TypeCheck(structTable, symbolTableList));
        currentBlock.addInstruction(new Store(sourceLoc, lvalType, lvalLoc));

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
            return AddEmptyReturnStmt(currentBlock);
        }
        else if (stmt instanceof ReturnStatement)
        {
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
        nodeList.add(exitNode);
        return exitNode;
    }

    private BasicBlock AddReturnStmt(ReturnStatement retStmt, BasicBlock currentBlock)
    {
        Value retExpVal = AddExpression(retStmt.getExpression(), currentBlock);
        currentBlock.addInstruction(new BrUncond(exitNode.label));
        currentBlock.successorList.add(exitNode);
        exitNode.predecessorList.add(currentBlock);
        Type retType = convertType(function.getRetType());
        exitNode.addInstruction(new Return(retType, retExpVal));
        nodeList.add(exitNode);
        return exitNode;
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

    private Value AddIdentifierExpression(IdentifierExpression exp, BasicBlock currentBlock)
    {
        String id = exp.getId();
        Type type = convertType(symbolTableList.typeOf(id));
        Value result = new StackLocation();
        Value pointer = new Local(id);
        currentBlock.addInstruction(new Load(type, result, pointer));
        return result;
    }

    private Value AddUnaryExpression(UnaryExpression uExp, BasicBlock currentBlock)
    {
        UnaryExpression.Operator op = uExp.getOperator();
        Value rVal = AddExpression(uExp.getOperand(), currentBlock);
        Value result = new StackLocation();

        switch(op)
        {
            case NOT:
                currentBlock.addInstruction(new Xor(new i32(), new Immediate("true"), rVal, result));
                return result;
            case MINUS:
                currentBlock.addInstruction(new Mult(result, new Immediate("-1"), rVal, new i32()));
                return result;
            default:
                System.err.println("No type matched unary expression");
        }
        return null;
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
        // get type (struct) of left expression
        Expression lExp = exp.getLeft();
        // TODO there's got to be a better way
        ast.type.Type lType = lExp.TypeCheck(structTable, symbolTableList);
        StructType structType = (StructType) lType;
        Type sType = convertType(lType);

        // add base address instruction
        Value baseAddr = new StackLocation();
        Value localLoc = AddExpression(lExp, currentBlock);
        currentBlock.addInstruction(new Load(sType, baseAddr, localLoc));

        // add offset address instruction
        Value offsetAddr = new StackLocation();
        StructEntry entry = structTable.get(structType.GetName());
        int index = entry.getFieldIndex(exp.getId());
        currentBlock.addInstruction(new Getelementptr(offsetAddr, sType, baseAddr, index));

        // add dot instruction
        Value result = new StackLocation();
        Type fieldType = convertType(entry.getType(exp.getId()));
        currentBlock.addInstruction(new Load(fieldType, result, offsetAddr));
        return result;
    }

    private Value AddExpression(ast.exp.Expression exp, BasicBlock currentBlock)
    {
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
            IdentifierExpression iExp = (IdentifierExpression) exp;
            return AddIdentifierExpression(iExp, currentBlock);
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
            UnaryExpression uExp = (UnaryExpression) exp;
            return AddUnaryExpression(uExp, currentBlock);
        }
        else
        {
            System.out.println("No expression pattern matched");
            System.exit(8);
            return null;
        }
    }

}
