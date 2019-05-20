package arm;

import java.io.PrintStream;

public interface Instruction {

    String getString();

    default void print(PrintStream stream)
    {
        stream.println("\t" + this.getString());
    }
}
