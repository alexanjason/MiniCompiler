package ast;
import ast.type.Type;

public interface Lvalue
{
    Type TypeCheck(StructTable structTable, SymbolTableList symbolTables);
}
