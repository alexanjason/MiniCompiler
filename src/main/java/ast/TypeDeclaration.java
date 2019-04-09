package ast;

import java.util.List;

public class TypeDeclaration
{
   protected final int lineNum;
   protected final String name;
   protected final List<Declaration> fields;

   public TypeDeclaration(int lineNum, String name, List<Declaration> fields)
   {
      this.lineNum = lineNum;
      this.name = name;
      this.fields = fields;
   }
}
