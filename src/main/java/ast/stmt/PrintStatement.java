package ast.stmt;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
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

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      expression.TypeCheck(structTable, symbolTableList);
      return false;
   }
}
