package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.*;

public class IntegerExpression
   extends AbstractExpression
{
   private final String value;

   public IntegerExpression(int lineNum, String value)
   {
      super(lineNum);
      this.value = value;
   }

   public String getValue()
   {
      return value;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      return new IntType();
   }
}
