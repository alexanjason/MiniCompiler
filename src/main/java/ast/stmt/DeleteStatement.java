package ast.stmt;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.exp.Expression;
import ast.type.*;

public class DeleteStatement
   extends AbstractStatement
{
   private final Expression expression;

   public DeleteStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Expression getExpression()
   {
      return expression;
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      Type expType = expression.TypeCheck(structTable, symbolTableList);
      if (!(expType instanceof StructType))
      {
         System.err.println(super.lineNum + ": can only delete structs");
         System.exit(4);
      }
      return false;
   }
}
