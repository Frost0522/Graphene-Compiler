package src;

public class printbody {
    Expression exp; 
    public printbody(Expression exp){
        this.exp = exp;
    }
    public void print(Expression exp){
        if (exp instanceof IdentifierExp){
            System.out.print("       Identifier:"+((IdentifierExp)exp).getid());
        }
        else if (exp instanceof FunctionCall){
            System.out.println("        Function call");
            System.out.println("             name  "+((FunctionCall)exp).id().getid());
            System.out.print("             args");
            for (Expression exps : ((FunctionCall)exp).args().value()){
                print(exps);
            }
        }

        else if (exp instanceof IfExp){
            System.out.print("        if");
            print(((IfExp)exp).condition());
            //System.out.println("");
            System.out.print("        then");
            //System.out.println("");
            print(((IfExp)exp).thenClause());
            System.out.print("        else");
            print(((IfExp)exp).elseClause());

        }
        else if (exp instanceof MinusExp){
            print(((MinusExp)exp).getleft());
            System.out.print("        minus");
            print(((MinusExp)exp).getright());
        }
        else if (exp instanceof DivideExp){
            print(((DivideExp)exp).getleft());
            System.out.print("        divided by");
            print(((DivideExp)exp).getright());
        }
        else if (exp instanceof PlusExp){
            print(((PlusExp)exp).getleft());
            System.out.print("        plus");
            print(((PlusExp)exp).getright());
        }
        else if (exp instanceof TimesExp){
            print(((TimesExp)exp).getleft());
            System.out.print("        times");
            print(((TimesExp)exp).getright());
        }
        else if (exp instanceof EqualityExp){
            print(((EqualityExp)exp).getleft());
            System.out.print("      Equals_To");
            print(((EqualityExp)exp).getright());
        }
        else if (exp instanceof LessThanExp){
            print(((LessThanExp)exp).getleft());
            System.out.print("        less than");
            print(((LessThanExp)exp).getright());
        }
        else if (exp instanceof OrExp){
            print(((OrExp)exp).getleft());
            System.out.print("        Or");
            print(((OrExp)exp).getright());
        }
        else if (exp instanceof AndExp){
            print(((AndExp)exp).getleft());
            System.out.print("        And");
            print(((AndExp)exp).getright());
        }
        else if (exp instanceof NegateExp){
            System.out.print("        Negate");
            print(((NegateExp)exp).getExp());
        }
        else if (exp instanceof NotExp){
            System.out.print("        Not");
            print(((NotExp)exp).getExp());
        }
        else if (exp instanceof NumberExp){
            System.out.println("        Integer_Literal:"+((NumberExp)exp).getnum());     
        }
        else if (exp instanceof NumberExp){
            System.out.println("        Boolean_Literal:"+((BooleanExp)exp).getBool());     
        }
         
    }
}
