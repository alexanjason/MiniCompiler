package ast;

import ast.type.Type;
import java.util.HashMap;

public class StructEntry {
    private HashMap<String, Type> fieldTable;

    StructEntry(HashMap<String, Type> table)
    {
        fieldTable = table;
    }

    public void AddField(String name, Type type)
    {
        fieldTable.put(name, type);
    }

    public Type getType(String field)
    {
        return fieldTable.get(field);
    }
}
