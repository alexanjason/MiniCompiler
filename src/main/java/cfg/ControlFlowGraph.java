package cfg;

import ast.*;
import ast.exp.*;
import ast.stmt.*;
import llvm.*;

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
    // TODO POPULATE THIS LIST!!!!

    // (directed) edges denote flow between blocks

    ControlFlowGraph(ast.Function func)
    {
        entryNode = new BasicBlock(null);
        exitNode = new BasicBlock(null); //TODO
        nodeList = new ArrayList<>();
        nodeList.add(entryNode);
        BuildCFG(func);
    }

    private void BuildCFG(ast.Function func)
    {
        // allocate params
        AddAllocateParamsInst(func.getParams());

        // allocate locals
        AddAllocateLocalsInst(func.getLocals());

        // create instructions from body
        AddBodyInst(func.getBody());
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
        System.err.println("Undealt with type ahhh panic");
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

    private void AddBodyInst(ast.stmt.Statement body)
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

            // TODO lval
            Value lvalLoc = AddlVal(lval, currentBlock);
            Value sourceLoc = AddExpression(source, currentBlock);

            // TODO type????
            Instruction inst = new Store(sourceLoc, new i32(), lvalLoc);

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
            //  Instruction inst =
            //  currentBlock.addInstruction(inst);

        }
        else if (stmt instanceof InvocationStatement)
        {
            InvocationStatement invStmt = (InvocationStatement) stmt;
            //Instruction inst = GetExpressionInst(invStmt.getExpression());
            //currentBlock.addInstruction(inst);
            AddExpression(invStmt.getExpression(), currentBlock);
            // TODO is this right?
        }
        else if (stmt instanceof PrintLnStatement)
        {
            //TODO
        }
        else if (stmt instanceof PrintStatement)
        {
            //TODO
        }
        else if (stmt instanceof ReturnEmptyStatement)
        {

            //currentBlock.addInstruction(new ReturnVoid());

            // TODO exit block
        }
        else if (stmt instanceof ReturnStatement)
        {
            ReturnStatement retStmt = (ReturnStatement) stmt;
            Instruction retExpInst = GetExpressionInst(retStmt.getExpression());
            // TODO how do I get the return type?
            //  currentBlock.addInstruction(new Return());

            // TODO exit block
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
            // TODO
            nodeList.add(thenExitNode);
        }
        if (elseEntryNode != elseExitNode)
        {
            // TODO
            nodeList.add(elseExitNode);
        }
        nodeList.add(exitNode);

        thenExitNode.successorList.add(exitNode);
        elseExitNode.successorList.add(exitNode);

        return exitNode;

    }

    private BasicBlock AddWhileStatement(WhileStatement stmt, BasicBlock currentBlock)
    {
        Instruction guardInst = GetExpressionInst(stmt.getGuard());

        // add guard to end of current block
        currentBlock.addInstruction(guardInst);

        // add guard to end of true block
        List<BasicBlock> predList = new ArrayList<>();
        predList.add(currentBlock);
        BasicBlock trueEntryNode = new BasicBlock(predList);

        Statement trueStmt = stmt.getBody();
        BasicBlock trueExitNode = AddStatement(trueStmt, trueEntryNode); // TODO is this right?

        trueExitNode.addInstruction(guardInst);

        // TODO comparison and branching

        // false block
        List<BasicBlock> exitPredList = new ArrayList<>();
        exitPredList.add(trueExitNode);
        exitPredList.add(currentBlock);
        BasicBlock exitNode = new BasicBlock(exitPredList);
        exitNode.successorList.add(trueExitNode);
        exitNode.successorList.add(currentBlock);

        return exitNode;
    }

    private Value AddBinaryExpression(BinaryExpression exp, BasicBlock currentBlock)
    {
        BinaryExpression.Operator op = exp.getOperator();
        if (op == BinaryExpression.Operator.TIMES)
        {
            Value leftLoc = AddExpression(exp.getLeft(), currentBlock);
            Value rightLoc = AddExpression(exp.getRight(), currentBlock);
            Value result = new StackLocation();
            currentBlock.addInstruction(new Mult(result, leftLoc, rightLoc, new i32()));
            return result;
        }
        else if (op == BinaryExpression.Operator.DIVIDE)
        {
            // TODO
        }
        else if (op == BinaryExpression.Operator.PLUS)
        {
            // TODO
        }
        else if (op == BinaryExpression.Operator.MINUS)
        {
            // TODO
        }
        else if (op == BinaryExpression.Operator.LT)
        {
            // TODO cmp
        }
        else if (op == BinaryExpression.Operator.GT)
        {
            // TODO cmp
        }
        else if (op == BinaryExpression.Operator.LE)
        {
            // TODO cmp
        }
        else if (op == BinaryExpression.Operator.GE)
        {
            // TODO cmp
        }
        else if (op == BinaryExpression.Operator.EQ)
        {
            // TODO cmp
        }
        else if (op == BinaryExpression.Operator.NE)
        {
            // TODO cmp
        }
        else if (op == BinaryExpression.Operator.AND)
        {
            // TODO
        }
        else if (op == BinaryExpression.Operator.OR)
        {
            // TODO
        }
        return null; // TODO remove
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
            // TODO
            return null;
        }
        else if (exp instanceof FalseExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof IdentifierExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof IntegerExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof InvocationExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof NewExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof NullExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof ReadExpression)
        {
            // TODO
            return null;
        }
        else if (exp instanceof TrueExpression)
        {
            // TODO
            return null;
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
        //return null;
    }

    private Value AddlVal(ast.Lvalue lVal, BasicBlock currentBlock)
    {
        // TODO
        return null;
    }

}
