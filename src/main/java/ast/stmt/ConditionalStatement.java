package ast.stmt;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.exp.Expression;
import ast.type.*;

public class ConditionalStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement thenBlock;
   private final Statement elseBlock;

   public ConditionalStatement(int lineNum, Expression guard,
      Statement thenBlock, Statement elseBlock)
   {
      super(lineNum);
      this.guard = guard;
      this.thenBlock = thenBlock;
      this.elseBlock = elseBlock;
   }

   public Expression getGuard()
   {
      return this.guard;
   }

   public Statement getThenBlock()
   {
      return this.thenBlock;
   }

   public Statement getElseBlock()
   {
      return this.elseBlock;
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      Type guardType = guard.TypeCheck(structTable, symbolTableList);
      Boolean thenRet = thenBlock.TypeCheck(structTable, symbolTableList, retType);
      Boolean elseRet = elseBlock.TypeCheck(structTable, symbolTableList, retType);

      if (guardType instanceof BoolType)
      {
         if (thenRet && elseRet)
         {
            return true;
         }
      }
      else
      {
         System.err.println(super.lineNum + ": non boolean guard");
         System.exit(5);
      }

      return false;
   }
}
