package ast.stmt;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.Type;

public interface Statement
{
    Boolean Returned();

    Type TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType);
}
