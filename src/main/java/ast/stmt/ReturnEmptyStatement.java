package ast.stmt;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

public class ReturnEmptyStatement
   extends AbstractStatement
{
   public ReturnEmptyStatement(int lineNum)
   {
      super(lineNum);
   }

   public Boolean Returned()
   {
      return true;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      if (retType instanceof VoidType)
      {
         return retType;
      }
      else
      {
         System.err.println(super.lineNum + ": non-void function returning void");
         System.exit(1);
      }
      return new ErrorType();
   }
}
