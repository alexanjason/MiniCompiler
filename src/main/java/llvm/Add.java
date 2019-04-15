package llvm;

public class Add implements Instruction {

    String left;
    String right;

    public Add(String left, String right)
    {
        this.left = left;
        this.right = right;
    }
}
