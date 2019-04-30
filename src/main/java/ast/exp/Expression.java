package ast.exp;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.Type;

public interface Expression
{
    Type TypeCheck(StructTable structTable, SymbolTableList symbolTables);

    int getLineNum();
}
