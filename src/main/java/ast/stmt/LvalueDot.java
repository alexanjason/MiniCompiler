package ast.stmt;
import ast.prog.StructEntry;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.exp.Expression;
import ast.type.*;

public class LvalueDot
   implements Lvalue
{
   private final int lineNum;
   private final Expression left;
   private final String id;

   public LvalueDot(int lineNum, Expression left, String id)
   {
      this.lineNum = lineNum;
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
      // evaluate Expression left
      Type leftType = left.TypeCheck(structTable, symbolTables);

      // check that left is a struct
      if (!(leftType instanceof StructType))
      {
         System.err.println(lineNum + ": Dot operator requires struct");
         System.exit(1);
      }

      // check that struct contains field
      String structName = ((StructType) leftType).GetName();
      StructEntry entry = structTable.get(structName);

      if (entry == null)
      {
         System.err.println("Struct " + structName + " not found");
         System.exit(1);
      }

      Type fieldType = entry.getType(id);
      if (fieldType == null)
      {
         System.err.println(lineNum + ": no field " + id + " in struct " + structName);
         System.exit(1);
      }

      // return type of field
      return fieldType;
   }
}
