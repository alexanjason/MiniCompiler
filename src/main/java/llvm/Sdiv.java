package llvm;

public class Sdiv implements Instruction {

    Value left;
    Value right;
    Type type;

    public Sdiv(Value left, Value right, Type type)
    {
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public String getString()
    {
        return ("sdiv " + type.getString() + left.getString() + ", " + right.getString());
    }
}
