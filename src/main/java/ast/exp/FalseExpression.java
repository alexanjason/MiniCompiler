package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class FalseExpression
   extends AbstractExpression
{
   public FalseExpression(int lineNum)
   {
      super(lineNum);
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      return new BoolType();
   }
}
