package ast.prog;

import ast.type.Type;
import ast.stmt.Statement;
import ast.type.VoidType;

import java.util.ArrayList;
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

   public List<Declaration> getParams()
   {
      return this.params;
   }

   public List<String> getParamNames()
   {
      List<String> list = new ArrayList<>();
      for (Declaration dec : params)
      {
         list.add(dec.getName());
      }
      return list;
   }

   public List<Declaration> getLocals()
   {
      return this.locals;
   }

   public Statement getBody()
   {
      return this.body;
   }

   public Type getRetType()
   {
      return retType;
   }

   public String getName()
   {
      return name;
   }

   public void TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      symbolTables.newScope();
      symbolTables.addDecls(params, Scope.PARAM);
      symbolTables.addDecls(locals, Scope.LOCAL);

      Boolean returned = body.TypeCheck(structTable, symbolTables, retType);

      if (!(returned))
      {
         if (!(retType instanceof VoidType))
         {
            System.err.println(lineNum + ": return not found in function");
            System.exit(3);
         }
      }
      symbolTables.removeScope();
   }

}
