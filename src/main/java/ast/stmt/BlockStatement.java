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

   public Boolean Returned()
   {
      for (Statement stmt : statements)
      {
         if (stmt.Returned())
         {
            return true;
         }
      }
      return false;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      for (Statement stmt : statements)
      {
         Type ret = stmt.TypeCheck(structTable, symbolTableList, retType);
         if (stmt instanceof ReturnEmptyStatement || stmt instanceof ReturnStatement)
         {
            if (ret.compareType(retType))
            {
               return ret;
            }
            else
            {
               System.err.println(super.lineNum + ": incorrect return type");
               System.exit(1);
            }
         }
      }

      return null;
   }

   public static BlockStatement emptyBlock()
   {
      return new BlockStatement(-1, new ArrayList<>());
   }
}
