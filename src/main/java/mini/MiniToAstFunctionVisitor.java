package mini;
import ast.prog.Declaration;
import ast.prog.Function;
import ast.type.*;
import ast.stmt.*;


import java.util.List;
import java.util.ArrayList;

public class MiniToAstFunctionVisitor
   extends MiniBaseVisitor<Function>
{
   private final MiniToAstTypeVisitor typeVisitor = new MiniToAstTypeVisitor();
   private final MiniToAstDeclarationsVisitor declarationsVisitor =
      new MiniToAstDeclarationsVisitor();
   private final MiniToAstStatementVisitor statementVisitor =
      new MiniToAstStatementVisitor();

   @Override
   public Function visitFunction(MiniParser.FunctionContext ctx)
   {
      return new Function(
         ctx.getStart().getLine(),
         ctx.ID().getText(),
         gatherParameters(ctx.parameters()),
         typeVisitor.visit(ctx.returnType()),
         declarationsVisitor.visit(ctx.declarations()),
         statementVisitor.visit(ctx.statementList()));
   }

   private List<Declaration> gatherParameters(
      MiniParser.ParametersContext ctx)
   {
      List<Declaration> params = new ArrayList<>();

      for (MiniParser.DeclContext dctx : ctx.decl())
      {
         params.add(new Declaration(dctx.getStart().getLine(),
            typeVisitor.visit(dctx.type()), dctx.ID().getText()));
      }

      return params;
   }

   @Override
   protected Function defaultResult()
   {
      return new Function(-1, "invalid", new ArrayList<>(),
         new VoidType(), new ArrayList<>(),
         BlockStatement.emptyBlock());
   }
}
