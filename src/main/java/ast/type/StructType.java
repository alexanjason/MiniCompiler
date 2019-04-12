package ast.type;

public class StructType implements Type
{
   private final int lineNum;
   private final String name;

   public StructType(int lineNum, String name)
   {
      this.lineNum = lineNum;
      this.name = name;
   }

   public String GetName()
   {
      return this.name;
   }

   public boolean compareType(Type type)
   {
      if (type instanceof StructType)
      {
         return this.name.equals(((StructType) type).name);
      }
      else
      {
         return type instanceof NullType;
      }
   }
}
