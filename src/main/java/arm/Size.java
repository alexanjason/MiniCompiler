package arm;

public class Size implements Instruction {

    String name;

    public Size(String name)
    {
        this.name = name;
    }

    public String getString()
    {
        return ".size " + name + ", .-" + name;
    }
}
