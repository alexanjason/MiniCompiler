package llvm;

public class Sdiv implements Instruction {

    String left;
    String right;

    public Sdiv(String left, String right)
    {
        this.left = left;
        this.right = right;
    }
}
