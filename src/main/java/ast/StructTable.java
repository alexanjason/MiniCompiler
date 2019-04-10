package ast;

import ast.type.Type;

import java.util.HashMap;

public class StructTable {
    protected static HashMap<String, StructEntry> table;

    StructTable(Program prog)
    {
        table = CreateStructTable(prog);
    }

    protected HashMap<String, StructEntry> CreateStructTable(Program prog)
    {
        HashMap<String, StructEntry> table = new HashMap<>();

        for (TypeDeclaration sdec : prog.types)
        {
            HashMap<String, Type> fieldMap = new HashMap<>();
            for (Declaration dec : sdec.fields)
            {
                fieldMap.put(dec.name, dec.type);
            }
            table.put(sdec.name, new StructEntry(fieldMap));
        }

        return table;
    }

    public boolean contains(String key)
    {
        return table.containsKey(key);
    }

    public StructEntry get(String key)
    {
        return table.get(key);
    }
}
