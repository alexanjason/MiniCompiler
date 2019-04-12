import mini.MiniCompiler;
import org.junit.*;

public class Benchmarks {

    //@Rule
    //public MiniCompiler mini = new MiniCompiler();

    @Test
    public void BenchMarkishTopics()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/BenchMarkishTopics/BenchMarkishTopics.mini";
        String[] args = {file};
        //mini.main(args);
        // TODO fix - continue looking in symbol table if find symbol of wrong type
    }

    @Test
    public void bert()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/bert/bert.mini";
        String[] args = {file};
        //mini.main(args);
        // TODO fix - look in symbol table if correct type not found in struct table
    }

    @Test
    public void biggest()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/biggest/biggest.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void binaryConverter()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/binaryConverter/binaryConverter.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void brett()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/brett/brett.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void creativeBenchMarkName()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/creativeBenchMarkName/creativeBenchMarkName.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void fact_sum()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/fact_sum/fact_sum.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void Fibonacci()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/Fibonacci/Fibonacci.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void GeneralFunctAndOptimize()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/GeneralFunctAndOptimize/GeneralFunctAndOptimize.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void hailstone()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/hailstone/hailstone.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void hanoi_benchmark()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/hanoi_benchmark/hanoi_benchmark.mini";
        String[] args = {file};
        mini.main(args);
    }

    @Test
    public void killerBubbles()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/killerBubbles/killerBubbles.mini";
        String[] args = {file};
        mini.main(args);
    }

}
