package ast;

import ast.type.Type;
import java.util.HashMap;

public class StructEntry {
    private HashMap<String, Type> FieldTable;

    StructEntry(HashMap<String, Type> table)
    {
        FieldTable = table;
    }

    public void AddField(String name, Type type)
    {
        FieldTable.put(name, type);
    }
}
