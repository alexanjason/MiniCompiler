package ast;
import ast.type.Type;

enum Scope
{
    GLOBAL, PARAM, LOCAL;
}

public class SymbolEntry {
    protected Type type;
    protected Scope scope;

    SymbolEntry(Type type, Scope scope)
    {
        this.type = type;
        this.scope = scope;
    }
}
