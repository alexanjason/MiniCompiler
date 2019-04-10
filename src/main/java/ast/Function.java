package ast;

import ast.type.Type;
import ast.stmt.Statement;
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
}
