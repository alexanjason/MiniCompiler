package cfg;

public class Label {

    static int increment = 0;
    private int id;

    public Label()
    {
        this.id = increment;
        increment++;
    }

    public String getString()
    {
        return ("L" + id);
    }
}
