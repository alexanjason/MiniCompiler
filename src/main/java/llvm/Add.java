package llvm;

public class Add implements Instruction {

    Value left;
    Value right;
    Type type;

    public Add(Value left, Value right, Type type)
    {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return ("add " + type.getString() + left.getString() + ", " + right.getString());
    }
}
