package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
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

   public int getLineNum() {
      return super.lineNum;
   }

   public List<Expression> getArguments()
   {
      return arguments;
   }

   public String getName()
   {
      return name;
   }

   public Type TypeCheck(StructTable structTable, SymbolTableList symbolTables)
   {
      if (!(symbolTables.contains(name)))
      {
         System.err.println(super.lineNum + ": function " + name + " not found");
         System.exit(4);
      }

      Type type = symbolTables.typeOf(name);

      if (!(type instanceof FunctionType))
      {
         System.err.println(super.lineNum + ": cannot invoke " + name + ", not a function");
         System.exit(4);
      }

      FunctionType funcType = (FunctionType) type;
      if (arguments.size() != funcType.numParams())
      {
         System.err.println(super.lineNum + ": incorrect number of parameters for function " + name);
         System.exit(7);
      }

      for (int i = 0; i < arguments.size(); i++)
      {
         Expression exp = arguments.get(i);
         Type expType = exp.TypeCheck(structTable, symbolTables);

         if (!(expType.compareType(funcType.getParamType(i))))
         {
            System.err.println(super.lineNum + ": incorrect parameter type ");
            System.exit(7);
         }
      }

      return funcType.getReturnType();
   }
}
