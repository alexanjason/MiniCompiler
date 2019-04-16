package ast.exp;

import ast.prog.StructEntry;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.StructType;
import ast.type.Type;

public class DotExpression
   extends AbstractExpression
{
   private final Expression left;
   private final String id;

   public DotExpression(int lineNum, Expression left, String id)
   {
      super(lineNum);
      this.left = left;
      this.id = id;
   }

   public Expression getLeft()
   {
      return left;
   }

   public String getId()
   {
      return id;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      // get type of expression left
      Type leftType = left.TypeCheck(structTable, symbolTables);

      // check that left is a struct
      if (!(leftType instanceof StructType))
      {
         System.err.println(lineNum + ": Dot operator requires struct");
         System.exit(4);
      }

      // check that struct exists in structTable
      String structName = ((StructType) leftType).GetName();
      StructEntry entry = structTable.get(structName);

      if (entry == null)
      {
         System.err.println(lineNum + ": struct " + structName + " not found");
         System.exit(4);
      }

      // check type of struct field
      Type fieldType = entry.getType(id);
      if (fieldType == null)
      {
         System.err.println(lineNum + ": no field " + id + " in struct " + structName);
         System.exit(4);
      }

      // return type of field
      return fieldType;
   }
}
