package ast.stmt;

import ast.prog.StructTable;
import ast.prog.SymbolTableList;
import ast.type.Type;

public interface Statement
{
    Boolean TypeCheck(StructTable structTable, SymbolTableList symbolTableList, Type retType);
}
