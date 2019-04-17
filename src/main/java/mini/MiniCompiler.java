package mini;
import ast.prog.Program;
import cfg.ControlFlowGraphList;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.PrintStream;
import java.util.regex.Pattern;

public class MiniCompiler
{
   public static void main(String[] args)
   {
      parseParameters(args);

      CommonTokenStream tokens = new CommonTokenStream(createLexer());
      MiniParser parser = new MiniParser(tokens);
      ParseTree tree = parser.program();

      if (parser.getNumberOfSyntaxErrors() == 0)
      {
         /*
            This visitor will build an object representation of the AST
            in Java using the provided classes.
         */
         MiniToAstProgramVisitor programVisitor = new MiniToAstProgramVisitor();
         Program program = programVisitor.visit(tree);

         program.TypeCheck();

         ControlFlowGraphList cfgList = new ControlFlowGraphList(program);
         cfgList.print(System.out); // for debugging
         // TODO clean this up
         System.out.println(_inputFile);
         String [] splitStr = _inputFile.split(Pattern.quote("."));
         /*
         for (String s : splitStr)
         {
            System.out.println(s);
         }
         */
         String llvmFile = splitStr[0] + ".ll";
         System.out.println(llvmFile);
         PrintStream fStream = null;
         try
         {
            fStream = new PrintStream(llvmFile);
         }
         catch (java.io.FileNotFoundException ex)
         {
            System.err.println("file not found");
            System.exit(-1);
         }
         cfgList.print(fStream);
      }
   }

   private static String _inputFile = null;

   private static void parseParameters(String [] args)
   {
      for (int i = 0; i < args.length; i++)
      {
         if (args[i].charAt(0) == '-')
         {
            System.err.println("unexpected option: " + args[i]);
            System.exit(1);
         }
         else if (_inputFile != null)
         {
            _inputFile = null;
            _inputFile = args[i];
            //System.err.println("too many files specified");
            //System.exit(1);
         }
         else
         {
            _inputFile = args[i];
         }
      }
   }

   private static void error(String msg)
   {
      System.err.println(msg);
      System.exit(1);
   }

   private static MiniLexer createLexer()
   {
      try
      {
         CharStream input;
         if (_inputFile == null)
         {
            input = CharStreams.fromStream(System.in);
         }
         else
         {
            input = CharStreams.fromFileName(_inputFile);
         }
         return new MiniLexer(input);
      }
      catch (java.io.IOException e)
      {
         System.err.println("file not found: " + _inputFile);
         System.exit(1);
         return null;
      }
   }
}
