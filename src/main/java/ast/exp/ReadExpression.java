package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class ReadExpression
   extends AbstractExpression
{
   public ReadExpression(int lineNum)
   {
      super(lineNum);
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      return new IntType();
   }
}
