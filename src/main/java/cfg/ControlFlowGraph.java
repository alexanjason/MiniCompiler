package cfg;

import ast.exp.*;
import ast.prog.*;
import ast.stmt.*;
import ast.type.FunctionType;
import ast.type.StructType;
import llvm.inst.*;
import llvm.type.*;
import llvm.type.Void;
import llvm.value.*;

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
        Value result = new StackLocation();
        Type type = convertType(function.getRetType());
        // TODO put this retval business in a class? Value?
        if (!(type instanceof Void))
        {
            Value retVal = new Local("%_retval_");
            entryNode.addInstruction(new Allocate("_retval_", type));
            exitNode.addInstruction(new Load(type, result, retVal));
            exitNode.addInstruction(new Return(type, result));
        }
        else
        {
            exitNode.addInstruction(new ReturnVoid());
        }

        nodeList = new ArrayList<>();
        nodeList.add(entryNode);
        BuildCFG();
    }

    private void BuildCFG()
    {
        // allocate params
        // TODO load params into alloca locs
        AddAllocateParamsInst(function.getParams());

        // allocate locals
        AddAllocateLocalsInst(function.getLocals());

        // create instructions from body
        AddBodyInst(function.getBody());
    }

    private void AddBodyInst(Statement body)
    {
        if (body instanceof BlockStatement)
        {
            BlockStatement block = (BlockStatement) body;
            BasicBlock lastBlock = AddBlockStatement(block, entryNode);
            if (lastBlock == null)
            {
                entryNode.addInstruction(new BrUncond(exitNode.label));
            }
            nodeList.add(exitNode);
        }
        else
        {
            System.err.println("Function body not a BlockStatement");
        }
    }

    private void AddAllocateParamsInst(List<Declaration> params)
    {
        for (Declaration dec : params)
        {
            // Create allocation instruction
            Type type = convertType(dec.getType());
            String param = "_P_" + dec.getName();
            Value localParam = new Local("%" + param);
            Instruction inst = new Allocate(param, type);

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);

            // load param into allocated location
            Value actualParam = new Local("%" + dec.getName());
            entryNode.addInstruction(new Store(actualParam, type, localParam));
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
            String name = sType.GetName();

             // TODO probs shouldn't be implemented here
            StructEntry entry = structTable.get(name);

            int size = 0;
            for (StructField field : entry.getFields())
            {
                size += 4;
            }

            return new Struct(name, size);
        }
        else if (astType instanceof ast.type.VoidType)
        {
            return new llvm.type.Void();
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

    private BasicBlock AddBlockStatement(BlockStatement block, BasicBlock currentBlock)
    {
        BasicBlock newCurBlock = currentBlock;

        if ((block.getLineNum() == -1) || (block.getStatements().size() == 0))
        {
            // TODO body is empty block
            return null;
        }
        for (Statement stmt : block.getStatements())
        {
            newCurBlock = AddStatement(stmt, newCurBlock);
        }
        return newCurBlock;
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
        Value localLoc = AddExpression(lExp, currentBlock);

        // add offset address instruction
        Value offsetAddr = new StackLocation();
        StructEntry entry = structTable.get(structType.GetName());
        int index = entry.getFieldIndex(exp.getId());
        currentBlock.addInstruction(new Getelementptr(offsetAddr, sType, localLoc, index));

        // add dot instruction
        Value result = new StackLocation();
        Type fieldType = convertType(entry.getType(exp.getId()));
        currentBlock.addInstruction(new Load(fieldType, result, offsetAddr));
        return result;
    }

    private Value AddlVal(Lvalue lVal, BasicBlock currentBlock)
    {
        if (lVal instanceof LvalueDot)
        {
            LvalueDot valDot = (LvalueDot) lVal;
            Expression lExp = valDot.getLeft();
            String id = valDot.getId();

            // get type (struct) of left expression
            ast.type.Type lType = lExp.TypeCheck(structTable, symbolTableList);
            StructType structType = (StructType) lType;
            Type sType = convertType(lType);

            Value localLoc;
            if (lExp instanceof IdentifierExpression)
            {
                IdentifierExpression iExp = (IdentifierExpression) lExp;
                localLoc = new Local(iExp.getId());
            }
            else
            {
                localLoc = AddExpression(lExp, currentBlock);
            }

            // add offset address instruction
            Value offsetAddr = new StackLocation();
            StructEntry entry = structTable.get(structType.GetName());
            int index = entry.getFieldIndex(id);
            currentBlock.addInstruction(new Getelementptr(offsetAddr, sType, localLoc, index));

            return offsetAddr;

        }
        else if (lVal instanceof LvalueId)
        {
            LvalueId valId = (LvalueId) lVal;
            String id = valId.getId();
            return getLocalFromId(id);
        }

        return null;
    }

    private BasicBlock AddDeleteStmt(DeleteStatement delStmt, BasicBlock currentBlock)
    {
        Value result = AddExpression(delStmt.getExpression(), currentBlock);
        // TODO reuse call? issue: free has no result
        currentBlock.addInstruction(new Free(result));
        return currentBlock;
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

    private BasicBlock AddPrintLnStmt(PrintLnStatement pStmt, BasicBlock currentBlock)
    {
        Value expVal = AddExpression(pStmt.getExpression(), currentBlock);
        // TODO implement PrintLn better
        currentBlock.addInstruction(new PrintLn(expVal, new i32()));
        return currentBlock;
    }

    private BasicBlock AddPrintStmt(PrintStatement pStmt, BasicBlock currentBlock)
    {
        Value expVal = AddExpression(pStmt.getExpression(), currentBlock);
        // TODO implement Print better
        currentBlock.addInstruction(new Print(expVal, new i32()));
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
            AddExpression(invStmt.getExpression(), currentBlock);
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
        System.err.println("add empty");
        currentBlock.addInstruction(new BrUncond(exitNode.label));
        currentBlock.successorList.add(exitNode);
        exitNode.predecessorList.add(currentBlock);
        exitNode.addInstruction(new ReturnVoid());
        return exitNode;
    }

    private BasicBlock AddReturnStmt(ReturnStatement retStmt, BasicBlock currentBlock)
    {
        Value retExpVal = AddExpression(retStmt.getExpression(), currentBlock);
        Value retVal = new Local("%_retval_");
        Type type = convertType(function.getRetType());
        currentBlock.addInstruction(new Store(retExpVal, type, retVal));
        currentBlock.addInstruction(new BrUncond(exitNode.label));
        currentBlock.successorList.add(exitNode);
        exitNode.predecessorList.add(currentBlock);
        return exitNode;
    }

    private BasicBlock AddConditionalStmt(ConditionalStatement stmt, BasicBlock currentBlock)
    {
        // create true block
        List<BasicBlock> predList = new ArrayList<>();
        predList.add(currentBlock);
        BasicBlock thenEntryNode = new BasicBlock(predList);
        // link true block to current block
        currentBlock.successorList.add(thenEntryNode);
        nodeList.add(thenEntryNode);
        BasicBlock thenExitNode = AddStatement(stmt.getThenBlock(), thenEntryNode);

        // false block
        BasicBlock elseEntryNode = new BasicBlock(predList);
        // link false block to current block
        currentBlock.successorList.add(elseEntryNode);
        nodeList.add(elseEntryNode);
        BasicBlock elseExitNode = AddStatement(stmt.getElseBlock(), elseEntryNode);

        // create exit block
        List<BasicBlock> predCondList = new ArrayList<>();
        predCondList.add(thenExitNode);
        if (elseExitNode != null)
        {
            predCondList.add(elseExitNode);
        }
        BasicBlock condExitNode = new BasicBlock(predCondList);

        if (thenExitNode != exitNode)
        {
            thenExitNode.addInstruction(new BrUncond(condExitNode.label));
        }

        // add guard to end of current block
        Value guardLoc = AddExpression(stmt.getGuard(), currentBlock);
        Label elseNodeLabel = elseEntryNode.label;
        if (elseEntryNode.instructions.size() == 0)
        {
            elseNodeLabel = condExitNode.label;
        }
        Value extGuard = new StackLocation();
        currentBlock.addInstruction(new Trunc(new i32(), guardLoc, new i1(), extGuard));
        currentBlock.addInstruction(new BrCond(extGuard, thenEntryNode.label, elseNodeLabel));//elseEntryNode.label));

        // create exit block
        nodeList.add(condExitNode);
        thenExitNode.successorList.add(condExitNode);

        if (elseExitNode != null)
        {
            elseExitNode.successorList.add(condExitNode);
            if (elseExitNode != exitNode)
            {
                elseExitNode.addInstruction(new BrUncond(condExitNode.label));
            }
        }

        return condExitNode;
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
        nodeList.add(falseNode);

        // Get value of guard and add to current block
        Value guardVal = AddExpression(stmt.getGuard(), currentBlock);
        Value extGuard = new StackLocation();
        currentBlock.addInstruction(new Trunc(new i32(), guardVal, new i1(), extGuard));
        Instruction brInst = new BrCond(extGuard, trueEntryNode.label, falseNode.label);
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
        Instruction callInst;
        if (retType instanceof Void)
        {
            callInst = new CallVoid(retType, fName, paramTypes, paramVals);
        }
        else
        {
            callInst = new Call(result, retType, fName, paramTypes, paramVals);
        }
        currentBlock.addInstruction(callInst);
        return result;
    }

    private Value getLocalFromId(String id)
    {
        Scope scope = symbolTableList.scopeOf(id);
        if (scope == Scope.PARAM)
        {
            return new Local("%_P_" + id);
        }
        else if (scope == Scope.GLOBAL)
        {
            return new Local("@" + id);
        }
        else
        {
            return new Local("%" + id);
        }
    }

    private Value AddIdentifierExpression(IdentifierExpression exp, BasicBlock currentBlock)
    {
        String id = exp.getId();
        Type type = convertType(symbolTableList.typeOf(id));
        Value result = new StackLocation();
        Value pointer = getLocalFromId(id);

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
            Value intResult = new StackLocation();
            currentBlock.addInstruction(new Icmp(intResult, "slt", new i32(), leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));
            return result;
        }
        else if (op == BinaryExpression.Operator.GT)
        {
            Value intResult = new StackLocation();
            currentBlock.addInstruction(new Icmp(intResult, "sgt", new i32(), leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));
            return result;
        }
        else if (op == BinaryExpression.Operator.LE)
        {
            Value intResult = new StackLocation();
            currentBlock.addInstruction(new Icmp(intResult, "sle", new i32(), leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));
            return result;
        }
        else if (op == BinaryExpression.Operator.GE)
        {
            Value intResult = new StackLocation();
            currentBlock.addInstruction(new Icmp(intResult, "sge", new i32(), leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));
            return result;
        }
        else if (op == BinaryExpression.Operator.EQ)
        {
            Value intResult = new StackLocation();
            currentBlock.addInstruction(new Icmp(intResult, "eq", new i32(), leftLoc, rightLoc));
            // TODO could be struct
            currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));

            return result;
        }
        else if (op == BinaryExpression.Operator.NE)
        {
            Value intResult = new StackLocation();
            currentBlock.addInstruction(new Icmp(intResult, "ne", new i32(), leftLoc, rightLoc));
            // TODO could be struct
            currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));

            return result;
        }
        else if (op == BinaryExpression.Operator.AND)
        {
            //Value intResult = new StackLocation();
            currentBlock.addInstruction(new And(result, new i32(), leftLoc, rightLoc));
            //currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));

            return result;
        }
        else if (op == BinaryExpression.Operator.OR)
        {
            //Value intResult = new StackLocation();
            currentBlock.addInstruction(new Or(result, new i32(), leftLoc, rightLoc));
            //currentBlock.addInstruction(new Zext(new i1(), intResult, new i32(), result));

            return result;
        }
        else
        {
            System.err.println("No match in BinaryExpression");
            System.exit(8);
            return null;
        }
    }

    private Value AddNewExpression(NewExpression exp, BasicBlock currentBlock)
    {
        StructEntry entry = structTable.get(exp.getId());
        // TODO there's a better way than converting type
        Type type = convertType(new StructType(-1, exp.getId()));

        List<Value> paramVals = new ArrayList<>();
        // TODO convert bit size to whatever malloc takes
        paramVals.add(new Immediate(Integer.toString(type.getSize())));
        List<Type> paramTypes = new ArrayList<>();
        paramTypes.add(new i32());

        Value ptr = new StackLocation();
        Value result = new StackLocation();
        currentBlock.addInstruction(new Call(ptr, new i8(), "malloc", paramTypes, paramVals));
        currentBlock.addInstruction(new Bitcast(new i8(), type, ptr, result));
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
            NewExpression newExp = (NewExpression) exp;
            return AddNewExpression(newExp, currentBlock);
        }
        else if (exp instanceof NullExpression)
        {
            return new Immediate("null");
        }
        else if (exp instanceof ReadExpression)
        {
            //Value ptrResult = new StackLocation();
            Value result = new StackLocation();
            currentBlock.addInstruction(new Read(result));
            //Value nResult = new StackLocation();
            //currentBlock.addInstruction(new Store(nResult, new i32(), result));
            return result;
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

    public void print(PrintStream stream)
    {
        printFunction(stream);
        for (BasicBlock block : nodeList)
        {
            block.print(stream);
        }
        stream.print("}\n");
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

}
