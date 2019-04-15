package llvm;

public class Add implements Instruction {

    Value result;
    Value left;
    Value right;
    Type type;

    public Add(Value result, Value left, Value right, Type type)
    {
        this.result = result;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return (result.getString() + " = add " + type.getString() + left.getString()
                + ", " + right.getString());
    }
}