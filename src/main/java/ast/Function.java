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

      boolean returned = false;
      body.TypeCheck(structTable, symbolTables, retType);

      symbolTables.removeScope();

      // TODO ensure void functions do not attempt to return value
      if (returned || retType instanceof VoidType)
      {
         return retType;
      }
      else
      {
         System.err.println("Return not found in function " + name + " line " + lineNum);
         System.exit(1);
         return new ErrorType();
      }
   }

}
