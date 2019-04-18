import mini.MiniCompiler;
import org.junit.*;

public class Benchmarks {

    //@Rule
    //TODO
    //public MiniCompiler mini = new MiniCompiler();

    @Test
    public void BenchMarkishTopics()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/BenchMarkishTopics/BenchMarkishTopics.mini";
        String[] args = {file};
        mini.main(args);
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/BenchMarkishTopics/BenchMarkishTopics.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void bert()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/bert/bert.mini";
        String[] args = {file};
        mini.main(args);
        // TODO null
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/bert/bert.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void biggest()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/biggest/biggest.mini";
        String[] args = {file};
        mini.main(args);
        // TODO read
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/biggest/biggest.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void binaryConverter()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/binaryConverter/binaryConverter.mini";
        String[] args = {file};
        mini.main(args);
        // TODO read
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/binaryConverter/binaryConverter.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void brett()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/brett/brett.mini";
        String[] args = {file};
        mini.main(args);
        // TODO true
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/brett/brett.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void creativeBenchMarkName()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/creativeBenchMarkName/creativeBenchMarkName.mini";
        String[] args = {file};
        mini.main(args);
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/creativeBenchMarkName/creativeBenchMarkName.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void fact_sum()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/fact_sum/fact_sum.mini";
        String[] args = {file};
        mini.main(args);
        // TODO bool size probs
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/fact_sum/fact_sum.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void Fibonacci()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/Fibonacci/Fibonacci.mini";
        String[] args = {file};
        mini.main(args);
        // TODO cond exit blocks
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/Fibonacci/Fibonacci.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void GeneralFunctAndOptimize()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/GeneralFunctAndOptimize/GeneralFunctAndOptimize.mini";
        String[] args = {file};
        mini.main(args);
        // TODO no percent on getelementptr when Local Value
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/GeneralFunctAndOptimize/GeneralFunctAndOptimize.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void hailstone()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/hailstone/hailstone.mini";
        String[] args = {file};
        mini.main(args);
        // TODO undefined label
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/hailstone/hailstone.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void hanoi_benchmark()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/hanoi_benchmark/hanoi_benchmark.mini";
        String[] args = {file};
        mini.main(args);
        // TODO same getelementptr bug
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/hanoi_benchmark/hanoi_benchmark.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void killerBubbles()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/killerBubbles/killerBubbles.mini";
        String[] args = {file};
        mini.main(args);
        // TODO same getelementptr bug
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/killerBubbles/killerBubbles.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void mile1()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/mile1/mile1.mini";
        String[] args = {file};
        mini.main(args);
        // TODO same read bug
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang mile1.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void mixed()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/mixed/mixed.mini";
        String[] args = {file};
        mini.main(args);
        // TODO empty block
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/mixed/mixed.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void OptimizationBenchmark()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/OptimizationBenchmark/OptimizationBenchmark.mini";
        String[] args = {file};
        mini.main(args);
        // TODO i1 type
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/OptimizationBenchmark/OptimizationBenchmark.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void primes()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/primes/primes.mini";
        String[] args = {file};
        mini.main(args);
        // TODO false
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/primes/primes.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void programBreaker()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/programBreaker/programBreaker.mini";
        String[] args = {file};
        mini.main(args);
        // TODO i1 issue
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/programBreaker/programBreaker.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void stats()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/stats/stats.mini";
        String[] args = {file};
        mini.main(args);
        // TODO same getelementptr bug
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/stats/stats.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void TicTac()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/TicTac/TicTac.mini";
        String[] args = {file};
        mini.main(args);
        // TODO getelementptr issue
        /*
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/TicTac/TicTac.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

    @Test
    public void wasteOfCycles()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "benchmarks/wasteOfCycles/wasteOfCycles.mini";
        String[] args = {file};
        // TODO read
        /*
        mini.main(args);
        try
        {
            Process c = Runtime.getRuntime().exec("clang benchmarks/wasteOfCycles/wasteOfCycles.ll");
            Process p = Runtime.getRuntime().exec("/a.out");
        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
        */
    }

}
