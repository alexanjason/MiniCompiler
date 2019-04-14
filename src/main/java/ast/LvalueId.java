package ast;

import ast.type.*;

public class LvalueId
   implements Lvalue
{
   private final int lineNum;
   private final String id;

   public LvalueId(int lineNum, String id)
   {
      this.lineNum = lineNum;
      this.id = id;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      if (symbolTables.contains(id))
      {
         return symbolTables.typeOf(id);
      }
      else if (id.equals("null"))
      {
         return new NullType();
      }

      return new ErrorType();

   }
}
