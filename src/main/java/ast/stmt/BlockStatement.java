package ast.stmt;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
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

   public List<Statement> getStatements()
   {
      return this.statements;
   }

   public int getLineNum()
   {
      return super.lineNum;
   }

   public static BlockStatement emptyBlock()
   {
      return new BlockStatement(-1, new ArrayList<>());
   }
}
