package ast.stmt;
import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.Type;

public interface Lvalue
{
    Type TypeCheck(StructTable structTable, SymbolTableList symbolTables);
}
