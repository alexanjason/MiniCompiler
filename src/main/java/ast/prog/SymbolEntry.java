package ast.prog;
import ast.type.Type;

public class SymbolEntry {
    protected Type type;
    protected Scope scope;

    SymbolEntry(Type type, Scope scope)
    {
        this.type = type;
        this.scope = scope;
    }
}
