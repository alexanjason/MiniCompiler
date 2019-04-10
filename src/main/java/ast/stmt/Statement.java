package ast.stmt;

import ast.StructTable;
import ast.SymbolTableList;
import ast.type.Type;

public interface Statement
{
    Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType);
}
