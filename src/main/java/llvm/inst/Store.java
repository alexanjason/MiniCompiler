package llvm.inst;

import llvm.type.Type;
import llvm.value.Value;

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
        System.out.println("Store 25");
        System.out.println("type: " + type.getString());
        System.out.println("value: " + value.getString());
        System.out.println("ptr: " + ptr.getString());
        return ("store " + type.getString() + "*" + value.toString() + ", " + type.getString() + "**" + ptr.getString());
    }

}
