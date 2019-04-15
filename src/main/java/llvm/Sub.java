package llvm;

public class Sub implements Instruction {

    Value left;
    Value right;
    Type type;

    public Sub (Value left, Value right, Type type)
    {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return ("sub " + type.getString() + left.getString() + ", " + right.getString());
    }
}
