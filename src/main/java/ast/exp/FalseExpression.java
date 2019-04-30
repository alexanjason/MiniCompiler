package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.*;

public class FalseExpression
   extends AbstractExpression
{
   public FalseExpression(int lineNum)
   {
      super(lineNum);
   }

   public int getLineNum() {
      return super.lineNum;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      return new BoolType();
   }
}
