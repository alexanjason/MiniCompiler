package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
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

   public int getLineNum() {
      return super.lineNum;
   }

   public String getId()
   {
      return id;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      // TODO what is valid in a new expression
      if (structTable.contains(id))
      {
         return new StructType(super.lineNum, id);
      }

      System.err.println("NewExpression 26");
      return null;
      //return new ErrorType();
   }
}
