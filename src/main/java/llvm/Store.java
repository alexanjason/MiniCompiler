package llvm;

public class Store implements Instruction {

    // store <ty> <value>, <ty>* <pointer>

    String ptr;
    String value;
    ast.type.Type type; //TODO

    // store %struct.foo* %u3, %struct.foo** %unused

    public Store(String value, ast.type.Type type, String ptr)
    {
        this.ptr = ptr;
        this.value = value;
        this.type = type;
    }

    public Store()
    {
        // TODO stub
    }

}
