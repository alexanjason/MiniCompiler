package ast;

import ast.type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program
{
   private final List<TypeDeclaration> types;
   private final List<Declaration> decls;
   private final List<Function> funcs;
   protected static HashMap<String, StructEntry> StructTable;
   protected static HashMap<String, SymbolEntry> SymbolTable;

   public Program(List<TypeDeclaration> types, List<Declaration> decls,
      List<Function> funcs)
   {
      this.types = types;
      this.decls = decls;
      this.funcs = funcs;
      StructTable = CreateStructTable();
      SymbolTable = CreateSymbolTable();
   }

   protected HashMap<String, StructEntry> CreateStructTable()
   {
      HashMap<String, StructEntry> table = new HashMap<>();

      for (TypeDeclaration sdec : types)
      {
         HashMap<String, Type> fieldMap = new HashMap<>();
         for (Declaration dec : sdec.fields)
         {
            fieldMap.put(dec.name, dec.type);
         }
         table.put(sdec.name, new StructEntry(fieldMap));

      }

      return table;
   }

   protected HashMap<String, SymbolEntry> CreateSymbolTable()
   {
      HashMap<String, SymbolEntry> table = new HashMap<>();

      for (Declaration dec : decls)
      {
         table.put(dec.name, new SymbolEntry(dec.type, Scope.GLOBAL));
      }

      boolean hasMain = false;
      for (Function func : funcs)
      {
         List<Type> paramList = new ArrayList<>();
         for (Declaration dec : func.params)
         {
            paramList.add(dec.type);
         }

         table.put(func.name, new SymbolEntry(new FunctionType(paramList, func.retType), Scope.GLOBAL));
         if (func.name.equals("main"))
         {
            hasMain = true;
         }
      }

      if (!hasMain)
      {
         System.err.println("No main function");
         System.exit(1);
      }

      return table;
   }
}
