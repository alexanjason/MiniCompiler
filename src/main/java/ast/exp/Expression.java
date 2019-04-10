package ast.exp;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.Type;

public interface Expression
{
    Type TypeCheck(StructTable structTable, SymbolTableList symbolTables);
}
