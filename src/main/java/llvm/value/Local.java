package llvm.value;

import llvm.type.Type;

public class Local implements Value {

    String id;
    Type type;

    public Local(String id, Type type)
    {
        this.id = id;
        this.type = type;
    }

    public String getString()
    {
        return ("%" + id);
    }

    public String getId()
    {
        return id;
    }

    public Type getType()
    {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Value)) {
            return false;
        }

        Value c = (Value) o;

        return this.getString().equals(c.getString());
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }
}
