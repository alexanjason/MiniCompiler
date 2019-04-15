package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class NewExpression
   extends AbstractExpression
{
   private final String id;

   public NewExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      // TODO what is valid in a new expression
      if (structTable.contains(id))
      {
         return new StructType(super.lineNum, id);
      }

      return new ErrorType();
   }
}
