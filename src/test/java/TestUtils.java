import mini.MiniCompiler;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class TestUtils {

    String readHelper = "../../main/resources/read_util.c";

    boolean stack;
    boolean llvm;
    boolean sscp;
    boolean uce;
    boolean lvn;

    File runCompiler(TemporaryFolder folder, String name, String dest)
    {
        File dir = null;
        try {
            dir = folder.newFolder("temp");
        }
        catch (Exception e)
        {
            System.err.println("Temp folder not created");
        }

        MiniCompiler mini = new MiniCompiler();

        int numArgs = 1;
        if (stack) { numArgs++; }
        if (llvm) { numArgs++; }
        if (sscp) { numArgs++; }
        if (uce) { numArgs++; }
        if (lvn) { numArgs++; }

        String[] args = new String[numArgs];
        args[0] = dest + name + ".mini";
        int i = 1;
        if (stack)
        {
            args[i] = "-stack";
            i++;
        }
        if (llvm)
        {
            args[i] = "-llvm";
            i++;
        }
        if (sscp)
        {
            args[i] = "-sscp";
            i++;
        }
        if (uce)
        {
            args[i] = "-uce";
            i++;
        }
        if (lvn)
        {
            args[i] = "-lvn";
            i++;
        }

        //String[] args = {dest + name + ".mini", "-stack", "-llvm"};
        mini.main(args);

        String file;
        if (llvm)
        {
            file = name + ".ll";
        }
        else
        {
            file = name + ".s";
        }

        try {
            Files.move(Paths.get(dest + file), Paths.get(dir + "/" + file));
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

        return dir;
    }

    void runClang(String file, File dir)
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

            BufferedReader input = new BufferedReader(new FileReader(inputFile.getCanonicalFile()));
            BufferedReader output = new BufferedReader(new FileReader(outputFile.getCanonicalFile()));

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

    public void runTest(TemporaryFolder folder, String name)
    {
        String loc = "benchmarks/" + name + "/";

        File dir = runCompiler(folder, name, loc);

        if (llvm)
        {
            runClang(name + ".ll", dir);
        }
        else
        {
            runClang(name + ".s", dir);
        }

        if (!(runAout(loc,"input", "output", dir)))
        {
            fail("a.out");
        }

        if (!(runAout(loc,"input.longer", "output.longer", dir)))
        {
            fail("a.out");
        }
    }
}
