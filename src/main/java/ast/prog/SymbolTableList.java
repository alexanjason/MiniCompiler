package ast.prog;

import ast.type.FunctionType;
import ast.type.Type;

import java.util.*;

public class SymbolTableList {
    protected static Deque<HashMap<String, SymbolEntry>> list;
    //TODO make this into hash table instead of a list

    SymbolTableList(Program prog)
    {
        this.list = new LinkedList<>();
        CreateSymbolTable(prog);
    }

    public boolean contains(String name)
    {
        // TODO optimize
        if (list.getFirst().containsKey(name))
        {
            return true;
        }
        else
        {
            for (HashMap<String, SymbolEntry> map : list)
            {
                if (map.containsKey(name) && map.get(name).scope == Scope.GLOBAL)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public Scope scopeOf(String name)
    {
        // TODO optimize
        if (list.getFirst().containsKey(name))
        {
            return list.getFirst().get(name).scope;
        }
        else
        {
            for (HashMap<String, SymbolEntry> map : list)
            {
                if (map.containsKey(name) && map.get(name).scope == Scope.GLOBAL)
                {
                    return map.get(name).scope;
                }
            }
        }

        return null;
    }

    public Type typeOf(String name)
    {
        // TODO optimize
        if (list.getFirst().containsKey(name))
        {
            return list.getFirst().get(name).type;
        }
        else
        {
            for (HashMap<String, SymbolEntry> map : list)
            {
                if (map.containsKey(name) && map.get(name).scope == Scope.GLOBAL)
                {
                    return map.get(name).type;
                }
            }
        }

        return null;
    }

    // TODO fix data structure and make this private
    public void removeScope()
    {
        list.removeFirst();
    }

    // TODO fix data structure and make this private
    public void newScope()
    {
        this.list.addFirst(new HashMap<>());
    }

    // TODO fix data structure and make this private
    public void addDecls(List<Declaration> decls, Scope scope)
    {
        HashMap<String, SymbolEntry> table = list.getFirst();
        for (Declaration dec : decls)
        {
            if(!table.containsKey(dec.name)){
                table.put(dec.name, new SymbolEntry(dec.type, scope));
            }
            else{
                System.err.println(dec.lineNum + ": Can't have two formal paramters for a function with the same name, " +
                        "or two variables local to a function with the same name");
                System.exit(1);
            }
        }
    }

    protected void CreateSymbolTable(Program prog)
    {
        this.newScope();
        this.addDecls(prog.decls, Scope.GLOBAL);

        //TODO Local  declarations  and  parameters  may  hide  global  declarations
        // (and  functions),  but  a  local  may  not redeclare a parameter.

        boolean hasMain = false;
        for (Function func : prog.funcs)
        {
            List<Type> paramList = new ArrayList<>();
            for (Declaration dec : func.params)
            {
                paramList.add(dec.type);
            }

            list.getFirst().put(func.name, new SymbolEntry(new FunctionType(paramList, func.retType), Scope.GLOBAL));
            //TODO Program execution begins in the function named main that takes
            // no arguments and that returns an int. Every valid program must have
            // such a function
            if (func.name.equals("main"))
            {
                hasMain = true;
            }
        }

        if (!hasMain)
        {
            System.err.println("No main function");
            System.exit(1);
        }
    }
}
