package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class TrueExpression
   extends AbstractExpression
{
   public TrueExpression(int lineNum)
   {
      super(lineNum);
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      return new BoolType();
   }
}
