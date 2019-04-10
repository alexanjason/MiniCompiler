package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.ErrorType;
import ast.type.NullType;
import ast.type.StructType;
import ast.type.Type;

public class IdentifierExpression
   extends AbstractExpression
{
   private final String id;

   public IdentifierExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      // TODO modularize this?
      if (structTable.contains(id))
      {
         return new StructType(super.lineNum, id);
      }
      if (symbolTables.contains(id))
      {
         return symbolTables.typeOf(id);
      }
      if (id.equals("null"))
      {
         return new NullType();
      }
      return new ErrorType();
   }
}
