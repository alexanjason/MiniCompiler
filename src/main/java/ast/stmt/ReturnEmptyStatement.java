package ast.stmt;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.*;

public class ReturnEmptyStatement
   extends AbstractStatement
{
   public ReturnEmptyStatement(int lineNum)
   {
      super(lineNum);
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      if (!(retType instanceof VoidType))
      {
         System.err.println(super.lineNum + ": non-void function returning void");
         System.exit(3);
      }
      return true;
   }
}
