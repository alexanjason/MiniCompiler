package cfg;

import ast.prog.StructEntry;
import ast.prog.StructTable;
import llvm.type.Struct;
import llvm.type.i32;

public class TypeConverter {

    static StructTable structTable;

    public TypeConverter(StructTable structTable)
    {
        this.structTable = structTable;
    }

    public static llvm.type.Type convertType(ast.type.Type astType)
    {
        if (astType instanceof ast.type.IntType)
        {
            return new i32();
        }
        else if (astType instanceof ast.type.BoolType)
        {
            return new i32();
        }
        else if (astType instanceof ast.type.StructType)
        {
            ast.type.StructType sType = (ast.type.StructType) astType;
            String name = sType.GetName();

            StructEntry entry = structTable.get(name);

            int size = entry.getFields().size() * 4;

            return new Struct(name, size);
        }
        else if (astType instanceof ast.type.VoidType)
        {
            return new llvm.type.Void();
        }

        System.err.println("Undealt with type");
        System.exit(8);
        return null;
    }
}
