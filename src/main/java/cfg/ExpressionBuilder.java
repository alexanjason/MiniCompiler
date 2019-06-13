package cfg;

import ast.exp.*;
import ast.prog.Scope;
import ast.prog.StructEntry;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.FunctionType;
import ast.type.StructType;
import llvm.inst.*;
import llvm.type.*;
import llvm.type.Void;
import llvm.value.*;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder {

    boolean stackBased;
    TypeConverter converter;
    SymbolTableList symbolTableList;
    StructTable structTable;
    protected List<Value> values;

    public ExpressionBuilder(boolean stackBased, TypeConverter converter, SymbolTableList symbolTableList,
                             StructTable structTable, List<Value> values)
    {
        this.stackBased = stackBased;
        this.converter = converter;
        this.symbolTableList = symbolTableList;
        this.structTable = structTable;
        this.values = values;
    }

    private Value AddIdentifierExpression(IdentifierExpression exp, BasicBlock currentBlock)
    {
        String id = exp.getId();
        Value result;

        if (stackBased)
        {
            Value pointer = getLocalFromId(id);
            if (stackBased)
            {
                result = new StackLocation(pointer.getType());
            }
            else
            {
                result = new Register(pointer.getType());
            }
            values.add(result);

            currentBlock.addInstruction(new Load(result, pointer));
        }
        else
        {
            Type type = converter.convertType(symbolTableList.typeOf(id));
            result = currentBlock.readVariable(id, type);
        }

        return result;
    }

    public Value getLocalFromId(String id)
    {
        Scope scope = symbolTableList.scopeOf(id);
        Type type = converter.convertType(symbolTableList.typeOf(id));

        Value val;
        if (scope == Scope.PARAM)
        {
            val = new Local("_P_" + id, type);
        }
        else if (scope == Scope.GLOBAL)
        {
            val = new Global(id, type);
        }
        else
        {
            val = new Local(id, type);
        }
        values.add(val);
        return val;
    }

    private Value AddUnaryExpression(UnaryExpression uExp, BasicBlock currentBlock)
    {
        UnaryExpression.Operator op = uExp.getOperator();
        Value rVal = AddExpression(uExp.getOperand(), currentBlock);
        Value result;
        if (stackBased)
        {
            result = new StackLocation(new i32());
        }
        else
        {
            result = new Register(new i32());
        }
        values.add(result);

        switch(op)
        {
            case NOT:
                Value extTrue, right;
                if (stackBased)
                {
                    extTrue = new StackLocation(new i1());
                }
                else
                {
                    extTrue = new Register(new i1());
                }
                values.add(extTrue);

                if (stackBased)
                {
                    right = new StackLocation(new i1());
                }
                else
                {
                    right = new Register(new i1());
                }
                values.add(right);

                //currentBlock.addInstruction(new Zext(new Immediate("true", new i1()), extTrue));
                currentBlock.addInstruction(new Trunc(rVal, right));
                currentBlock.addInstruction(new Xor(new Immediate("true", new i1()), right, extTrue));
                currentBlock.addInstruction(new Zext(extTrue, result));

                return result;
            case MINUS:
                currentBlock.addInstruction(new Mult(result, new Immediate("-1", new i32()), rVal));
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
        Value result;
        if (stackBased)
        {
            result = new StackLocation(new i32());
        }
        else
        {
            result = new Register(new i32());
        }
        values.add(result);

        if (op == BinaryExpression.Operator.TIMES)
        {
            currentBlock.addInstruction(new Mult(result, leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.DIVIDE)
        {
            currentBlock.addInstruction(new Sdiv(result, leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.PLUS)
        {
            currentBlock.addInstruction(new Add(result, leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.MINUS)
        {
            currentBlock.addInstruction(new Sub(result, leftLoc, rightLoc));
            return result;
        }
        else if (op == BinaryExpression.Operator.LT)
        {
            Value intResult;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            currentBlock.addInstruction(new Icmp(intResult, "slt", leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(intResult, result));
            return result;
        }
        else if (op == BinaryExpression.Operator.GT)
        {
            Value intResult;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            currentBlock.addInstruction(new Icmp(intResult, "sgt", leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(intResult, result));
            return result;
        }
        else if (op == BinaryExpression.Operator.LE)
        {
            Value intResult;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            currentBlock.addInstruction(new Icmp(intResult, "sle", leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(intResult, result));
            return result;
        }
        else if (op == BinaryExpression.Operator.GE)
        {
            Value intResult;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            currentBlock.addInstruction(new Icmp(intResult, "sge", leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(intResult, result));
            return result;
        }
        else if (op == BinaryExpression.Operator.EQ)
        {
            Value intResult;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            currentBlock.addInstruction(new Icmp(intResult, "eq", leftLoc, rightLoc));
            // TODO could be struct
            currentBlock.addInstruction(new Zext(intResult, result));

            return result;
        }
        else if (op == BinaryExpression.Operator.NE)
        {
            Value intResult;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            currentBlock.addInstruction(new Icmp(intResult, "ne", leftLoc, rightLoc));
            currentBlock.addInstruction(new Zext(intResult, result));

            return result;
        }
        else if (op == BinaryExpression.Operator.AND)
        {
            Value intResult, left, right;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);
            if (stackBased)
            {
                left  = new StackLocation(new i1());
            }
            else
            {
                left = new Register(new i1());
            }
            values.add(left);
            if (stackBased)
            {
                right  = new StackLocation(new i1());
            }
            else
            {
                right = new Register(new i1());
            }
            values.add(right);

            currentBlock.addInstruction(new Trunc(leftLoc, left));
            currentBlock.addInstruction(new Trunc(rightLoc, right));
            currentBlock.addInstruction(new And(intResult, left, right));
            currentBlock.addInstruction(new Zext(intResult, result));
            return result;
        }
        else if (op == BinaryExpression.Operator.OR)
        {
            Value intResult, left, right;
            if (stackBased)
            {
                intResult  = new StackLocation(new i1());
            }
            else
            {
                intResult = new Register(new i1());
            }
            values.add(intResult);

            if (stackBased)
            {
                left  = new StackLocation(new i1());
            }
            else
            {
                left = new Register(new i1());
            }
            values.add(left);
            if (stackBased)
            {
                right  = new StackLocation(new i1());
            }
            else
            {
                right = new Register(new i1());
            }
            values.add(right);

            currentBlock.addInstruction(new Trunc(leftLoc, left));
            currentBlock.addInstruction(new Trunc(rightLoc, right));
            currentBlock.addInstruction(new Or(intResult, left, right));
            currentBlock.addInstruction(new Zext(intResult, result));
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
        // TODO there's a better way than converting type
        Type type = converter.convertType(new StructType(-1, exp.getId()));

        List<Value> paramVals = new ArrayList<>();
        paramVals.add(new Immediate(Integer.toString(type.getSize()), type));
        List<Type> paramTypes = new ArrayList<>();
        paramTypes.add(new i32());

        Value ptr;
        Value result;
        if (stackBased)
        {
            ptr = new StackLocation(new i8());
            result = new StackLocation(type);
        }
        else
        {
            ptr = new Register(new i8());
            result = new Register(type);
        }
        values.add(ptr);
        values.add(result);

        currentBlock.addInstruction(new Call(ptr, "malloc", paramTypes, paramVals));
        currentBlock.addInstruction(new Bitcast(ptr, result));
        return result;
    }

    public Value AddExpression(ast.exp.Expression exp, BasicBlock currentBlock)
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
            Value result;
            if (stackBased)
            {
                result = new StackLocation(new i32());
            }
            else
            {
                result = new Register(new i32());
            }
            values.add(result);

            currentBlock.addInstruction(new Zext(new Immediate("false", new i1()) , result));
            return result;
        }
        else if (exp instanceof IdentifierExpression)
        {
            IdentifierExpression iExp = (IdentifierExpression) exp;
            return AddIdentifierExpression(iExp, currentBlock);
        }
        else if (exp instanceof IntegerExpression)
        {
            return new Immediate(((IntegerExpression) exp).getValue(), new i32());
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
            return new Immediate("null", new Void());
        }
        else if (exp instanceof ReadExpression)
        {
            Value result;
            if (stackBased)
            {
                result = new StackLocation(new i32());
            }
            else
            {
                result = new Register(new i32());
            }
            values.add(result);

            List<Value> emptyValList = new ArrayList<>();
            List<Type> emptyTypeList = new ArrayList<>();
            currentBlock.addInstruction(new Call(result,"read_util", emptyTypeList, emptyValList));
            return result;
        }
        else if (exp instanceof TrueExpression)
        {
            Value result;
            if (stackBased)
            {
                result = new StackLocation(new i32());
            }
            else
            {
                result = new Register(new i32());
            }
            values.add(result);

            currentBlock.addInstruction(new Zext(new Immediate("true", new i1()), result));
            return result;
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

    private Value AddDotExpression(DotExpression exp, BasicBlock currentBlock)
    {
        // get type (struct) of left expression
        Expression lExp = exp.getLeft();

        // add base address instruction
        Value localLoc = AddExpression(lExp, currentBlock);
        Type sType = localLoc.getType();

        // add offset address instruction
        Value offsetAddr;
        if (stackBased)
        {
            offsetAddr = new StackLocation(sType);
        }
        else
        {
            offsetAddr = new Register(sType);
        }
        values.add(offsetAddr);

        StructEntry entry = structTable.get(((Struct)sType).getName());
        int index = entry.getFieldIndex(exp.getId());
        currentBlock.addInstruction(new Getelementptr(offsetAddr, localLoc, index));

        // add dot instruction
        Type fieldType = converter.convertType(entry.getType(exp.getId()));
        Value result;
        if (stackBased)
        {
            result = new StackLocation(fieldType);
        }
        else
        {
            result = new Register(fieldType);
        }
        values.add(result);

        currentBlock.addInstruction(new Load(result, offsetAddr));

        return result;
    }

    private Value AddInvocationExpression(InvocationExpression exp, BasicBlock currentBlock)
    {
        List<Type> paramTypes = new ArrayList<>();
        List<Value> paramVals = new ArrayList<>();
        String fName = exp.getName();
        FunctionType fType = (FunctionType) symbolTableList.typeOf(fName);
        Type retType = converter.convertType(fType.getReturnType());

        int i = 0;
        for (Expression pExp : exp.getArguments())
        {
            paramVals.add(AddExpression(pExp, currentBlock));
            paramTypes.add(converter.convertType(fType.getParamType(i)));
            i++;
        }

        Instruction callInst;
        Value result = null;
        if (retType instanceof Void)
        {
            callInst = new CallVoid(fName, paramTypes, paramVals);
        }
        else
        {
            if (stackBased)
            {
                result = new StackLocation(retType);
            }
            else
            {
                result = new Register(retType);
            }
            values.add(result);
            callInst = new Call(result, fName, paramTypes, paramVals);
        }

        currentBlock.addInstruction(callInst);
        return result;
    }
}
