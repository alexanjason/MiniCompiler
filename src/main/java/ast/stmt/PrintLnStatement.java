package ast.stmt;
import ast.StructTable;
import ast.SymbolTableList;
import ast.exp.Expression;
import ast.type.Type;

public class PrintLnStatement
   extends AbstractStatement
{
   private final Expression expression;

   public PrintLnStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      expression.TypeCheck(structTable, symbolTableList);
      return false;
   }
}
