package llvm;

public class Mult implements Instruction {

    Value left;
    Value right;
    Type type;

    public Mult(Value left, Value right, Type type)
    {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return ("mul " + type.getString() + left.getString() + ", " + right.getString());
    }
}
