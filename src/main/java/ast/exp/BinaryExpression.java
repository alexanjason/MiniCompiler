package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.*;

public class BinaryExpression
   extends AbstractExpression
{
   private final Operator operator;
   private final Expression left;
   private final Expression right;

   private BinaryExpression(int lineNum, Operator operator,
      Expression left, Expression right)
   {
      super(lineNum);
      this.operator = operator;
      this.left = left;
      this.right = right;
   }

   public int getLineNum() {
      return super.lineNum;
   }

   public Operator getOperator()
   {
      return operator;
   }

   public Expression getLeft()
   {
      return this.left;
   }

   public Expression getRight()
   {
      return this.right;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      // get types of left and right
      Type lType = left.TypeCheck(structTable, symbolTables);
      Type rType = right.TypeCheck(structTable, symbolTables);

      // equality require integers or structures (both sides must be same type)
      if (operator == Operator.EQ || operator == Operator.NE)
      {
         if (lType instanceof IntType || lType instanceof StructType || lType instanceof NullType)
         {
            if (lType.compareType(rType))
            {
               return new BoolType();
            }
            else
            {
               System.err.println(super.lineNum + ": equality operators require same types");
               System.exit(4);

            }
         }
         else
         {
            System.err.println(super.lineNum + ": equality operators require integers or structures");
            System.exit(4);
         }
      }

      // boolean require booleans
      else if (operator == Operator.AND || operator == Operator.OR)
      {
         if (lType instanceof BoolType && rType instanceof BoolType)
         {
            return new BoolType();
         }
         else
         {
            System.err.println(super.lineNum + ": boolean operators require booleans");
            System.exit(4);
         }
      }

      // arithmetic and relational require integers
      else
      {
         if (lType instanceof IntType && rType instanceof IntType)
         {
            if (operator == Operator.DIVIDE || operator == Operator.TIMES ||
                    operator == Operator.MINUS || operator == Operator.PLUS) {
               return new IntType();
            }
            else
            {
               return new BoolType();
            }
         }
         else
         {
            System.err.println(super.lineNum + ": arithmetic and relational operators require integers");
            System.exit(4);
         }
      }

      System.err.println("BinaryExpression 102");
      return null;
   }

   public static BinaryExpression create(int lineNum, String opStr,
      Expression left, Expression right)
   {
      switch (opStr)
      {
         case TIMES_OPERATOR:
            return new BinaryExpression(lineNum, Operator.TIMES, left, right);
         case DIVIDE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.DIVIDE, left, right);
         case PLUS_OPERATOR:
            return new BinaryExpression(lineNum, Operator.PLUS, left, right);
         case MINUS_OPERATOR:
            return new BinaryExpression(lineNum, Operator.MINUS, left, right);
         case LT_OPERATOR:
            return new BinaryExpression(lineNum, Operator.LT, left, right);
         case LE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.LE, left, right);
         case GT_OPERATOR:
            return new BinaryExpression(lineNum, Operator.GT, left, right);
         case GE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.GE, left, right);
         case EQ_OPERATOR:
            return new BinaryExpression(lineNum, Operator.EQ, left, right);
         case NE_OPERATOR:
            return new BinaryExpression(lineNum, Operator.NE, left, right);
         case AND_OPERATOR:
            return new BinaryExpression(lineNum, Operator.AND, left, right);
         case OR_OPERATOR:
            return new BinaryExpression(lineNum, Operator.OR, left, right);
         default:
            throw new IllegalArgumentException();
      }
   }

   private static final String TIMES_OPERATOR = "*";
   private static final String DIVIDE_OPERATOR = "/";
   private static final String PLUS_OPERATOR = "+";
   private static final String MINUS_OPERATOR = "-";
   private static final String LT_OPERATOR = "<";
   private static final String LE_OPERATOR = "<=";
   private static final String GT_OPERATOR = ">";
   private static final String GE_OPERATOR = ">=";
   private static final String EQ_OPERATOR = "==";
   private static final String NE_OPERATOR = "!=";
   private static final String AND_OPERATOR = "&&";
   private static final String OR_OPERATOR = "||";

   public static enum Operator
   {
      TIMES, DIVIDE, PLUS, MINUS, LT, GT, LE, GE, EQ, NE, AND, OR
   }
}
