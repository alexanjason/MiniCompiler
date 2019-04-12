package ast.stmt;
import ast.StructTable;
import ast.SymbolTableList;
import ast.exp.Expression;
import ast.Lvalue;
import ast.type.ErrorType;
import ast.type.Type;

public class AssignmentStatement
   extends AbstractStatement
{
   private final Lvalue target;
   private final Expression source;

   public AssignmentStatement(int lineNum, Lvalue target, Expression source)
   {
      super(lineNum);
      this.target = target;
      this.source = source;
   }

   public Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType)
   {
      // get type of lvalue
      Type lType = target.TypeCheck(structTable, symbolTableList);

      // get type of expression
      Type rType = source.TypeCheck(structTable, symbolTableList);

      // compare types (implemented in type?)
      if (!(lType.compareType(rType)))
      {
         System.out.println(super.lineNum + ": assignment, type mismatch");
         System.exit(4);
      }
      return false;
   }
}
