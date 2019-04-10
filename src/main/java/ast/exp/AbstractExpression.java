package ast.exp;

public abstract class AbstractExpression
   implements Expression
{
   protected final int lineNum;

   public AbstractExpression(int lineNum)
   {
      this.lineNum = lineNum;
   }
}
