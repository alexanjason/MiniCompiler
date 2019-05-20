package cfg;

public class Label {

    static int increment = 0;
    private int id;

    public Label()
    {
        this.id = increment;
        increment++;
    }

    public int getId()
    {
        return this.id;
    }

    public String getString()
    {
        return ("LU" + id);
    }
}
