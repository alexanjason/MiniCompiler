package ast.stmt;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.ErrorType;
import ast.type.Type;

import java.util.List;
import java.util.ArrayList;

public class BlockStatement
   extends AbstractStatement
{
   private final List<Statement> statements;

   public BlockStatement(int lineNum, List<Statement> statements)
   {
      super(lineNum);
      this.statements = statements;
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      Boolean ret = false;
      for (Statement stmt : statements)
      {
         Boolean stmtRet = stmt.TypeCheck(structTable, symbolTableList, retType);
         if (stmtRet)
         {
            ret = true;
         }
      }

      return ret;
   }

   public static BlockStatement emptyBlock()
   {
      return new BlockStatement(-1, new ArrayList<>());
   }
}
