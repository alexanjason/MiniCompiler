import mini.MiniCompiler;
import org.junit.*;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


public class Benchmarks {

    //@Rule
    //TODO
    //public MiniCompiler mini = new MiniCompiler();

    String readHelper = "../../../../main/resources/read_util.c";

    void runClang(String wd, String file)
    {
        ProcessBuilder clang = new ProcessBuilder("clang", file, readHelper);
        File dir = new File(wd).getAbsoluteFile();
        clang.redirectErrorStream(true);
        clang.directory(dir);
        try
        {
            Process p = clang.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null)
            {
                System.out.println(line);
            }

        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            System.exit(-2);
        }
    }

    boolean runAout(String wd)
    {
        ProcessBuilder aout = new ProcessBuilder("./a.out");
        File inputFile = new File(wd + "/" + "input");
        File outputFile = new File(wd + "/" + "output");
        File dir = new File(wd).getAbsoluteFile();
        aout.redirectErrorStream(true);
        aout.directory(dir);
        try
        {
            Process p = aout.start();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader input = new BufferedReader(new FileReader(inputFile));
            BufferedReader output = new BufferedReader(new FileReader(outputFile));

            String line;
            while ((line = input.readLine()) != null)
            {
                writer.write(line + "\n");
            }

            writer.flush();
            writer.close();

            String aLine;
            while ((line = output.readLine()) != null)
            {
                aLine = reader.readLine();
                assertEquals("Output doesn't match", line, aLine);
            }

        }
        catch(Exception e)
        {
            System.err.println("exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Test
    public void BenchMarkishTopics()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/BenchMarkishTopics";
        String file = "BenchMarkishTopics";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }

        // TODO null
    }

    @Test
    public void bert()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/bert";
        String file = "bert";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO null

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void biggest()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/biggest";
        String file = "biggest";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO getelementptr %

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void binaryConverter()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/binaryConverter";
        String file = "binaryConverter";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");


        if (!(runAout(dir)))
        {
            fail("a.out");
        }

    }

    @Test
    public void brett()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/brett";
        String file = "brett";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO undefined label

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }

    }

    @Test
    public void creativeBenchMarkName()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/creativeBenchMarkName";
        String file = "creativeBenchMarkName";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
        // TODO getelementptr % maybe
    }

    @Test
    public void fact_sum()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "fact_sum";
        String dir = "benchmarks/fact_sum";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void Fibonacci()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/Fibonacci";
        String file = "Fibonacci";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        // TODO cond exit blocks
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void GeneralFunctAndOptimize()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/GeneralFunctAndOptimize";
        String file = "GeneralFunctAndOptimize";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        // TODO no percent on getelementptr when Local Value
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void hailstone()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/hailstone";
        String file = "hailstone";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO undefined label
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void hanoi_benchmark()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/hanoi_benchmark";
        String file = "hanoi_benchmark";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO same getelementptr bug
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void killerBubbles()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/killerBubbles";
        String file = "killerBubbles";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO same getelementptr bug
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void mile1()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/mile1";
        String file = "mile1";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO getelementptr bug
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void mixed()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/mixed";
        String file = "mixed";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO empty block
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void OptimizationBenchmark()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/OptimizationBenchmark";
        String file = "OptimizationBenchmark";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");


        if (!(runAout(dir)))
        {
            fail("a.out");
        }

        // TODO some incorrect output
    }

    @Test
    public void primes()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/primes";
        String file = "primes";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void programBreaker()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/programBreaker";
        String file = "programBreaker";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);

        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void stats()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/stats";
        String file = "stats";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO same getelementptr bug
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void TicTac()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/TicTac";
        String file = "TicTac";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO getelementptr issue
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

    @Test
    public void wasteOfCycles()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/wasteOfCycles";
        String file = "wasteOfCycles";
        String[] args = {dir + "/" + file + ".mini"};
        mini.main(args);
        // TODO some incorrect output
        runClang(dir, file + ".ll");

        if (!(runAout(dir)))
        {
            fail("a.out");
        }
    }

}
