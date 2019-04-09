package ast;
import ast.type.Type;

public class Declaration
{
   protected final int lineNum;
   protected final Type type;
   protected final String name;

   public Declaration(int lineNum, Type type, String name)
   {
      this.lineNum = lineNum;
      this.type = type;
      this.name = name;
   }
}
