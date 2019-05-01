import mini.MiniCompiler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;


public class StackBasedBenchmarks {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    //TODO
    //public MiniCompiler mini = new MiniCompiler();

    String readHelper = "../../main/resources/read_util.c";

    File runClang(String file)
    {
        File helper = new File(readHelper);
        String path = null;
        try
        {
            path = helper.getCanonicalPath();
        }
        catch (Exception e)
        {
            System.err.println("can't get helper path");
        }

        ProcessBuilder clang = new ProcessBuilder("clang", file, path);
        clang.redirectErrorStream(true);

        File dir = null;
        try {
            dir = folder.newFolder("temp");
        }
        catch (Exception e)
        {
            System.err.println("Temp folder not created");
        }

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
        return dir;
    }

    boolean runAout(String wd, String in, String out, File dir)
    {
        ProcessBuilder aout = new ProcessBuilder("./a.out");
        File inputFile = new File(wd + "/" + in);
        File outputFile = new File(wd + "/" + out);

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
                assertEquals(output + " doesn't match", line, aLine);
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
        {
            fail("a.out");
        }
    }

    @Test
    public void bert()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/bert";
        String file = "bert";
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);
        // TODO returning void when no explicit return

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        // TODO voodoo?

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
        {
            fail("a.out");
        }
    }

    @Test
    public void fact_sum()
    {
        MiniCompiler mini = new MiniCompiler();
        String file = "fact_sum";
        String dir = "benchmarks/fact_sum";
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        // TODO undefined label: cond exit blocks
        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        // TODO ret void but also instructions after ret void (weird!)

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);
        // TODO voodoo todo created this bug

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        // TODO voodoo?

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
        {
            fail("a.out");
        }

        // TODO incorrect output
    }

    @Test
    public void primes()
    {
        MiniCompiler mini = new MiniCompiler();
        String dir = "benchmarks/primes";
        String file = "primes";
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        // TODO returning void when no explicit return

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
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
        String[] args = {dir + "/" + file + ".mini", "-stack"};
        mini.main(args);

        File fileAbs = new File(dir + "/" + file + ".ll");
        String abs = null;
        try
        {
            abs = fileAbs.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.err.println("can't get absolute path");
        }

        File fol = runClang(abs);

        if (!(runAout(dir,"input", "output", fol)))
        {
            fail("a.out");
        }

        if (!(runAout(dir,"input.longer", "output.longer", fol)))
        {
            fail("a.out");
        }
    }

}
