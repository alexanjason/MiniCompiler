package llvm;

public class Store implements Instruction {

    // store <ty> <value>, <ty>* <pointer>

    Value ptr;
    Value value;
    Type type; //TODO

    // store %struct.foo* %u3, %struct.foo** %unused

    public Store(Value value, Type type, Value ptr)
    {
        this.ptr = ptr;
        this.value = value;
        this.type = type;
    }

    public String getString()
    {
        return ("store " + type.getString() + "*" + value.toString() + ", " + type.getString() + "**" + ptr.getString());
    }

}
