package ast;

import ast.type.ErrorType;
import ast.type.Type;
import ast.stmt.Statement;
import ast.type.VoidType;

import java.util.HashMap;
import java.util.List;

public class Function
{
   protected final int lineNum;
   protected final String name;
   protected final Type retType;
   protected final List<Declaration> params;
   protected final List<Declaration> locals;
   protected final Statement body;

   public Function(int lineNum, String name, List<Declaration> params,
      Type retType, List<Declaration> locals, Statement body)
   {
      this.lineNum = lineNum;
      this.name = name;
      this.params = params;
      this.retType = retType;
      this.locals = locals;
      this.body = body;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      symbolTables.newScope();
      symbolTables.addDecls(params, Scope.PARAM);
      symbolTables.addDecls(locals, Scope.LOCAL);

      Type actualRetType = body.TypeCheck(structTable, symbolTables, retType);

      symbolTables.removeScope();

      if (body.Returned())// || retType instanceof VoidType)
      {
         /*
         if (actualRetType.compareType(retType))
         {
            return retType;
         }
         else
         {
            System.err.println(retType);
            System.err.println(actualRetType);
            System.err.println(lineNum + ": return type inconsistent with function declaration");
            System.exit(1);
            return new ErrorType();
         }
         */
         // TODO checking already done in statement type checking?
         return retType;
      }
      else
      {
         System.err.println(lineNum + ": return not found in function");
         System.exit(1);
      }
      return new ErrorType();
   }

}
