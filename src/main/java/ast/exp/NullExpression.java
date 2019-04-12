package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class NullExpression
   extends AbstractExpression
{
   public NullExpression(int lineNum)
   {
      super(lineNum);
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      return new NullType();

   }
}