package ast;

import ast.type.Type;

import java.util.HashMap;
import java.util.List;

public class Program
{
   private final List<TypeDeclaration> types;
   private final List<Declaration> decls;
   private final List<Function> funcs;
   protected static HashMap<String, StructEntry> StructTable;


   public Program(List<TypeDeclaration> types, List<Declaration> decls,
      List<Function> funcs)
   {
      this.types = types;
      this.decls = decls;
      this.funcs = funcs;
      StructTable = CreateStructTable();
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
}
