package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
//import ast.type.ErrorType;
import ast.type.NullType;
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
      if (symbolTables.contains(id))
      {
         return symbolTables.typeOf(id);
      }
      if (id.equals("null"))
      {
         return new NullType();
      }
      System.err.println("IdentifierExpression 31");
      return null;
      //return new ErrorType();
   }

   public String getId()
   {
      return this.id;
   }
}
