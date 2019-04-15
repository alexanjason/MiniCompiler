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
                if(!fieldMap.containsKey(dec.name)) {
                    fieldMap.put(dec.name, dec.type);
                }
                else{
                    System.err.println(dec.lineNum + ": Can't have declarations with same name");
                    System.exit(2);
                }
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
