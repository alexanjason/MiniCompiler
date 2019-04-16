package ast.prog;

import ast.type.Type;

import java.util.Collection;
import java.util.HashMap;

public class StructEntry {

    private HashMap<String, StructField> fieldTable;

    StructEntry(HashMap<String, StructField> table)
    {
        fieldTable = table;
    }

    public Type getType(String field)
    {
        return fieldTable.get(field).type;
    }

    public int getFieldIndex(String field)
    {
        return fieldTable.get(field).index;
    }

    public Collection<StructField> getFields()
    {
        return fieldTable.values();
    }

}
