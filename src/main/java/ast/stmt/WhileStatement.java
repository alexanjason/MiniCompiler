package ast.stmt;

import ast.StructTable;
import ast.SymbolTableList;
import ast.exp.Expression;
import ast.type.*;

public class WhileStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement body;

   public WhileStatement(int lineNum, Expression guard, Statement body)
   {
      super(lineNum);
      this.guard = guard;
      this.body = body;
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      Type guardType = guard.TypeCheck(structTable, symbolTableList);

      if (guardType instanceof BoolType)
      {
         body.TypeCheck(structTable, symbolTableList, retType);
      }
      else
      {
         System.out.println(super.lineNum + ": non boolean guard");
         System.exit(1);
      }
      return false;
   }
}
