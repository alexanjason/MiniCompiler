
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static junit.framework.TestCase.fail;

public class RegisterLLVMSSCPBenchmarks extends TestUtils {

    @Before
    public void setUp()
    {
        stack = false;
        llvm = true;
        sscp = true;
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void BenchMarkishTopics()
    {
        String name = "BenchMarkishTopics";
        runTest(folder, name);
    }

    @Test
    public void bert()
    {
        String name = "bert";
        // works on pi server
        runTest(folder, name);
    }

    @Test
    public void biggest()
    {
        String name = "biggest";
        runTest(folder, name);
    }

    @Test
    public void binaryConverter()
    {
        String name = "binaryConverter";
        runTest(folder, name);
    }

    @Test
    public void brett()
    {
        String name = "brett";
        // works on pi server
        runTest(folder, name);
    }

    @Test
    public void fact_sum()
    {
        String name = "fact_sum";
        runTest(folder, name);
    }

    @Test
    public void Fibonacci()
    {
        String name = "Fibonacci";
        runTest(folder, name);
    }

    @Test
    public void GeneralFunctAndOptimize()
    {
        String name = "GeneralFunctAndOptimize";
        fail("TODO");
        //runTest(folder, name);
    }

    @Test
    public void hailstone()
    {
        String name = "hailstone";
        runTest(folder, name);
    }

    @Test
    public void hanoi_benchmark()
    {
        String name = "hanoi_benchmark";
        runTest(folder, name);
    }

    @Test
    public void killerBubbles()
    {
        String name = "killerBubbles";
        runTest(folder, name);
    }

    @Test
    public void mile1()
    {
        String name = "mile1";
        fail("TODO");
        //runTest(folder, name);
    }

    @Test
    public void mixed()
    {
        String name = "mixed";
        runTest(folder, name);
    }

    @Test
    public void OptimizationBenchmark()
    {
        String name = "OptimizationBenchmark";
        fail("TODO");
        //runTest(folder, name);
    }

    @Test
    public void primes()
    {
        String name = "primes";
        //runTest(folder, name);
        fail("TODO");
    }

    @Test
    public void programBreaker()
    {
        String name = "programBreaker";
        runTest(folder, name);
    }

    @Test
    public void stats()
    {
        String name = "stats";
        fail("TODO");
        //runTest(folder, name);
    }

    @Test
    public void TicTac()
    {
        String name = "TicTac";
        runTest(folder, name);
    }

    @Test
    public void wasteOfCycles()
    {
        String name = "wasteOfCycles";
        runTest(folder, name);
    }
}
