package ast.stmt;
import ast.StructTable;
import ast.SymbolTableList;
import ast.exp.Expression;
import ast.type.Type;

public class PrintStatement
   extends AbstractStatement
{
   private final Expression expression;

   public PrintStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Boolean Returned()
   {
      return false;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      return expression.TypeCheck(structTable, symbolTableList);
   }
}
