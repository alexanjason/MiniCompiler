package ast.stmt;
import ast.StructTable;
import ast.SymbolTableList;
import ast.exp.Expression;
import ast.type.*;

public class ReturnStatement
   extends AbstractStatement
{
   private final Expression expression;

   public ReturnStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Boolean Returned()
   {
      return true;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      if (retType.compareType(expression.TypeCheck(structTable, symbolTableList)))
      {
         return retType;
      }
      else
      {
         System.err.println(super.lineNum + ": return type not consistent with function declaration");
         System.exit(1);
      }
      return new ErrorType();
   }
}
