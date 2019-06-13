package ast.prog;

import java.util.*;

public class Program
{
   protected final List<TypeDeclaration> types;
   protected final List<Declaration> decls;
   protected final List<Function> funcs;
   protected static StructTable structTable;
   protected static SymbolTableList symbolTables;

   public Program(List<TypeDeclaration> types, List<Declaration> decls, List<Function> funcs)
   {
      this.types = types;
      this.decls = decls;
      this.funcs = funcs;
      structTable = new StructTable(this);
      symbolTables = new SymbolTableList(this);
   }

   public List<Function> getFuncs()
   {
      return this.funcs;
   }

   public List<Declaration> getDecls()
   {
      return this.decls;
   }

   public List<TypeDeclaration> getTypes()
   {
      return this.types;
   }

   public StructTable getStructTable()
   {
      return structTable;
   }

   public SymbolTableList getSymbolTables()
   {
      return symbolTables;
   }

   public void TypeCheck()
   {
      for (Function func : funcs)
      {
         func.TypeCheck(structTable, symbolTables);
      }
   }
}
