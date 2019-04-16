package ast.stmt;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
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

   public String getId()
   {
      return this.id;
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

      System.err.println("LValueId 28");
      return null;
      //return new ErrorType();

   }
}
