package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.*;

import java.util.List;

public class InvocationExpression
   extends AbstractExpression
{
   private final String name;
   private final List<Expression> arguments;

   public InvocationExpression(int lineNum, String name,
      List<Expression> arguments)
   {
      super(lineNum);
      this.name = name;
      this.arguments = arguments;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      if (!(symbolTables.contains(name)))
      {
         System.err.println(super.lineNum + ": function " + name + " not found");
         System.exit(1);
      }

      Type type = symbolTables.typeOf(name);
      if (!(type instanceof FunctionType))
      {
         System.err.println(super.lineNum + ": cannot invoke " + name + ", not a function");
         System.exit(1);
      }

      FunctionType funcType = (FunctionType) type;
      if (arguments.size() != funcType.numParams())
      {
         System.err.println(super.lineNum + ": incorrect number of parameters for function " + name);
         System.exit(1);
      }


      for (int i = 0; i < arguments.size(); i++)
      {
         Expression exp = arguments.remove(i);
         Type expType = exp.TypeCheck(structTable, symbolTables);

         if (!(expType.compareType(funcType.getParamType(i))))
         {
            System.err.println(super.lineNum + " incorrect parameter type");
            System.exit(1);
         }
      }

      return funcType.getReturnType();
   }
}