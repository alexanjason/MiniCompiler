package llvm;

public class Mult implements Instruction {

    String left;
    String right;

    public Mult(String left, String right)
    {
        this.left = left;
        this.right = right;
    }
}
