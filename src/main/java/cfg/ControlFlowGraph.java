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

    // (directed) edges denote flow between blocks

    ControlFlowGraph(ast.Function func)
    {
        entryNode = new BasicBlock(null);
        exitNode = new BasicBlock(null); //TODO
        nodeList = new ArrayList<>();
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
            Instruction inst = new llvm.Allocate("_P_" + dec.getName(), dec.getType());

            // Add allocation instruction to entry node
            entryNode.addInstruction(inst);
        }
    }

    private void AddAllocateLocalsInst(List<Declaration> locals)
    {
        for (Declaration dec : locals)
        {
            // Create allocation instruction
            Instruction inst = new llvm.Allocate(dec.getName(), dec.getType());

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
            // TODO
            newCurBlock = AddStatement(stmt, newCurBlock);
        }
        return currentBlock;

    }

    private BasicBlock AddStatement(ast.stmt.Statement stmt, BasicBlock currentBlock)
    {
        if (stmt instanceof AssignmentStatement)
        {
            AssignmentStatement assignStmt = (AssignmentStatement) stmt;
            Lvalue lval = assignStmt.getTarget();
            Expression source = assignStmt.getSource();

            Instruction inst = new Store(); // TODO

            currentBlock.addInstruction(inst);
        }
        else if (stmt instanceof BlockStatement)
        {
            BlockStatement blockStmt = (BlockStatement) stmt;
            return AddBlockStatement(blockStmt, currentBlock);
        }
        else if (stmt instanceof ConditionalStatement)
        {
            ConditionalStatement condStmt = (ConditionalStatement) stmt;
            AddConditionalStmt(condStmt, currentBlock);
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
            Instruction inst = GetExpressionInst(invStmt.getExpression());
            currentBlock.addInstruction(inst);
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
            currentBlock.addInstruction(new ReturnVoid());
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
        // add guard to end of current block
        currentBlock.addInstruction(GetExpressionInst(stmt.getGuard()));
        // TODO comparison and branching

        // true block
        List<BasicBlock> predList = new ArrayList<>();
        predList.add(currentBlock);
        BasicBlock thenEntryNode = new BasicBlock(predList);
        BasicBlock thenExitNode = AddStatement(stmt.getThenBlock(), thenEntryNode);

        // false block
        BasicBlock elseEntryNode = new BasicBlock(predList);
        BasicBlock elseExitNode = AddStatement(stmt.getElseBlock(), elseEntryNode);

        // create exit block
        List<BasicBlock> predCondList = new ArrayList<>();
        predCondList.add(thenExitNode);
        predCondList.add(elseExitNode);
        BasicBlock exitNode = new BasicBlock(predCondList);

        // join blocks
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

    private Instruction GetBinaryExpressionInst(BinaryExpression exp)
    {
        /*
           private final Operator operator;
           private final Expression left;
           private final Expression right;
         */
        // TIMES, DIVIDE, PLUS, MINUS, LT, GT, LE, GE, EQ, NE, AND, OR
        BinaryExpression.Operator op = exp.getOperator();
        if (op == BinaryExpression.Operator.TIMES)
        {
            // TODO what to pass as left and right
            //return new Mult(String left, String right);
        }
        else if (op == BinaryExpression.Operator.DIVIDE)
        {
            // TODO what to pass as left and right
            //return new Sdiv(String left, String right);
        }
        else if (op == BinaryExpression.Operator.PLUS)
        {
            // TODO what to pass as left and right
            //return new Add(String left, String right);
        }
        else if (op == BinaryExpression.Operator.MINUS)
        {
            // TODO what to pass as left and right
            //return new Sub(String left, String right);
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
            // TODO what to pass as left and right
            //return new And(String left, String right);
        }
        else if (op == BinaryExpression.Operator.OR)
        {
            // TODO what to pass as left and right
            //return new Or(String left, String right);
        }
        return null; // TODO remove
    }

    private Instruction GetExpressionInst(ast.exp.Expression exp)
    {
        // TODO could expressions yield multiple instructions?

        if (exp instanceof BinaryExpression)
        {
            // TODO
            return null;
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

}
