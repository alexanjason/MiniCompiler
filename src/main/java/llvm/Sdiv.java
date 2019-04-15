package llvm;

public class Sdiv implements Instruction {

    Value result;
    Value left;
    Value right;
    Type type;

    public Sdiv(Value result, Value left, Value right, Type type)
    {
        this.result = result;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return (result.getString() + " = sdiv " + type.getString() + left.getString()
                + ", " + right.getString());
    }
}
