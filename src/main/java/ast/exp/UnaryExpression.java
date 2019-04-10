package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class UnaryExpression
   extends AbstractExpression
{
   private final Operator operator;
   private final Expression operand;

   private UnaryExpression(int lineNum, Operator operator, Expression operand)
   {
      super(lineNum);
      this.operator = operator;
      this.operand = operand;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      Type operandType = operand.TypeCheck(structTable, symbolTables);
      switch(operator)
      {
         case NOT:
            if (operandType instanceof BoolType)
            {
               return new BoolType();
            }
            else
            {
               System.err.println(super.lineNum + ": not operator requires boolean operand");
               System.exit(1);
            }
         case MINUS:
            if (operandType instanceof IntType)
            {
               return new IntType();
            }
            else
            {
               System.err.println(super.lineNum + ": minus operator requires integer operand");
               System.exit(1);
            }
         default:
            return new ErrorType();

      }
   }

   public static UnaryExpression create(int lineNum, String opStr,
      Expression operand)
   {
      if (opStr.equals(NOT_OPERATOR))
      {
         return new UnaryExpression(lineNum, Operator.NOT, operand);
      }
      else if (opStr.equals(MINUS_OPERATOR))
      {
         return new UnaryExpression(lineNum, Operator.MINUS, operand);
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }

   private static final String NOT_OPERATOR = "!";
   private static final String MINUS_OPERATOR = "-";

   public static enum Operator
   {
      NOT, MINUS
   }
}
