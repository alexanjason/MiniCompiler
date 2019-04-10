package ast.stmt;

import ast.StructTable;
import ast.SymbolTableList;
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

   public Boolean Returned()
   {
      // TODO what if one block returns and the other doesn't?
      if (thenBlock.Returned() && elseBlock.Returned())
      {
         return true;
      }
      // TODO ???
      return false;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      Type guardType = guard.TypeCheck(structTable, symbolTableList);
      Type thenType = thenBlock.TypeCheck(structTable, symbolTableList, retType);
      Type elseType = elseBlock.TypeCheck(structTable, symbolTableList, retType);

      if (guardType instanceof BoolType)
      {
         // TODO then and else only have to have same type if returning?
         // TODO if not returning what are their type?
         if ((elseType != null) && (thenType.compareType(elseType)))
         {
            return thenType;
         }
         else
         {
            if (thenBlock.Returned() && elseBlock.Returned())
            {
               System.err.println(super.lineNum + ": then and else must have same return type");
               System.exit(1);
            }
         }
      }
      else
      {
         System.err.println(super.lineNum + ": non boolean guard");
         System.exit(1);
      }
      return new ErrorType();
   }
}
