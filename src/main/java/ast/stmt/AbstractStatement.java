package ast.stmt;

public abstract class AbstractStatement
   implements Statement
{
   protected final int lineNum;

   public AbstractStatement(int lineNum)
   {
      this.lineNum = lineNum;
   }
}
