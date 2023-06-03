package src;
import java.util.ArrayList;
import java.util.Collection;
public interface AST_Node
{
}


class Definition_List implements AST_Node
{
    public ArrayList<DefinitionNode> definition_list;

    public Definition_List()
    {
        definition_list = new ArrayList<DefinitionNode>();
    }

    public void add(DefinitionNode Defnode)
    {
        this.definition_list.add(0, Defnode);
    }

    public DefinitionNode get(int i)
    {
        return this.definition_list.get(i);
    }

    public ArrayList<DefinitionNode> value()
    {
        return definition_list;
    }

    public String pretty_print() {
        String word_str = "";
        for (int i = 0; i < definition_list.size(); i++) {
          word_str += definition_list.get(i).pretty_print();
        }
        return word_str;
      }
    
      public String toString() {
        String word_str = "";
        for (int i = 0; i < definition_list.size(); i++) {
          word_str += definition_list.get(i).toString();
        }
        return word_str;
      }

}


class DefinitionNode implements AST_Node
{
    public IdentifierExp identifer;
    public ParameterList parameter_list;
    public TypeNode type;
    public Body body;

    public DefinitionNode(IdentifierExp id, ParameterList pl, TypeNode type, Body body)
    {
        this.identifer = id;
        this.parameter_list = pl;
        this.type = type;
        this.body = body;
    }

    public IdentifierExp id() {return identifer;}

    public ParameterList pl() {return parameter_list;}

    public TypeNode type() {return type;}

    public Body body() {return body;}

    public String pretty_print() {
        String word_str = "   name " + id().getid() + "\n" + "   params " + pl().pretty_print() + "\n" + "   returns " + type().Value() + "\n" + "   body " + body().pretty_print() + "\n";
        return word_str;
      }
    
      /*public String toString() {
        return word() + " = " + value();
      }
      */
}

class ParameterList implements AST_Node
{
    public ArrayList<IdWithType> formal_parameters;

    public ParameterList()
    {
        formal_parameters = new ArrayList<IdWithType>();
    }

    public void add(IdWithType idtnode)
    {
        this.formal_parameters.add(0, idtnode);
    }

    public IdWithType get(int i)
    {
        return this.formal_parameters.get(i);
    }

    public ArrayList<IdWithType> value()
    {
        return formal_parameters;
    }

    public String pretty_print() {
        String word_str = "";
        for (int i = 0; i < formal_parameters.size(); i++) {
          word_str += formal_parameters.get(i).pretty_print();
        }
        return word_str;
      }
    
      public String toString() {
        String word_str = "";
        for (int i = 0; i < formal_parameters.size(); i++) {
          word_str += formal_parameters.get(i).toString();
        }
        return word_str;
      }

}


class IdWithType implements AST_Node
{
    public IdentifierExp identifier;
    public TypeNode type;

    public IdWithType(IdentifierExp id, TypeNode type)
    {
        this.identifier = id;
        this.type = type;
    }

    public IdentifierExp id() {return identifier;}

    public TypeNode type() {return type;}

    public String pretty_print() {
        String word_str = id().getid() + " : " + type.Value()+" ";
        return word_str;
      }
}

class TypeNode implements AST_Node
{
    public enum Type{IntegerType, BooleanType;}
    private Type type;

    public TypeNode(Type type)
    {
        this.type = type;
    }

    public String Value() {
        if (type == Type.IntegerType){
            return "integer";
        }
        else {
            return "boolean";
        }
        
    }
}



class Body implements AST_Node
{
    private ArrayList<Expression> expression;

    public Body(ArrayList<Expression> exp)
    {
        this.expression = exp;
    }


    public ArrayList<Expression> exp () {return expression;}


    public String pretty_print() {
        String word_str = "";
        for (int i = 0; i < expression.size(); i++) {
          word_str += expression.get(i).pretty_print();
        }
        return word_str;
      }
    
      public String toString() {
        String word_str = "";
        for (int i = 0; i < expression.size(); i++) {
          word_str += expression.get(i).toString();
        }
        return word_str;
      }
      
}

class Expression implements AST_Node
{
    public String pretty_print() {
        return null;
        
      }
}


class BinaryExp extends Expression
{
    private Expression right;
    private Expression left;
    public BinaryExp(Expression left, Expression right)
    {
        this.right = right;
        this.left = left;
    }


    public Expression getright(){return right;}

    public Expression getleft(){return left;}

    public String pretty_print() {
        String word_str =  left + "binary" + right;
        return word_str;
      }


}

class EqualityExp extends BinaryExp
{
    EqualityExp(Expression left, Expression right)
    {
        super(left, right);
    }
    //public Expression getleft(){return left;}
    public String getequal(){
        return "=";
    }
}

class LessThanExp extends BinaryExp
{
    LessThanExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class OrExp extends BinaryExp
{
    OrExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class PlusExp extends BinaryExp
{
    PlusExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class MinusExp extends BinaryExp
{
    MinusExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class AndExp extends BinaryExp
{
    AndExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class TimesExp extends BinaryExp
{
    TimesExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class DivideExp extends BinaryExp
{
    DivideExp(Expression left, Expression right)
    {
        super(left, right);
    }
}

class UnaryExp extends Expression
{
    private Expression unar_exp;
    public UnaryExp(Expression unar)
    {
        this.unar_exp = unar;
    }
    public Expression getExp(){return unar_exp;}
    public String pretty_print() {
        String word_str = "   unar" + unar_exp;
        return word_str;
      }
}

class NotExp extends UnaryExp
{
    NotExp(Expression unar)
    {
        super(unar);
    }
}

class NegateExp extends UnaryExp
{
    NegateExp(Expression exp)
    {
        super(exp);
    }
}

class IfExp extends Expression
{
    private Expression condition;
    private Expression thenClause;
    private Expression elseClause;
    public IfExp(Expression condition, Expression thenClause, Expression elseClause)
    {
        this.condition = condition;
        this.thenClause = thenClause;
        this.elseClause = elseClause;
    }

    public Expression condition(){return condition;}

    public Expression thenClause(){return thenClause;}

    public Expression elseClause(){return elseClause;}

    public String pretty_print() {
        String word_str = "\n"+"      if  "+ condition.pretty_print() +"\n"+"       "+ "then  " + thenClause +"\n"+"      "+ "else " + elseClause;
        return word_str;
      }
}

class FunctionCall extends Expression
{
    ArgumentList arg;
    IdentifierExp id;

    public FunctionCall(IdentifierExp id, ArgumentList args)
    {
        this.arg = args;
        this.id = id;
    }

    public IdentifierExp id(){return id;}
    public ArgumentList args(){return arg;}
    

    public String pretty_print() {
        String word_str = "\n" + "     function call" + "\n" + "      name   " + id().getid() + "\n" + "      args" + arg.pretty_print() +"\n";
        return word_str;
      }

}

class IdentifierExp extends Expression
{
    private String id;
    public IdentifierExp(String exp)
    {
        this.id = exp;
    }

    public String getid(){return id;}
    public String pretty_print() {
        String word_str = "   Identifier:" + id;
        return word_str;
      }

}



class NumberExp extends Expression
{
    private String num;
    public NumberExp(String exp)
    {
        this.num = exp;
    }

    public String getnum(){return num;}
    public String pretty_print() {
        String word_str = "     Integer_literal " + num;
        return word_str;
      }
}

class BooleanExp extends Expression
{
    private String Boolean;
    public BooleanExp(String exp)
    {
        this.Boolean = exp;
    }

    public String getBool(){return Boolean;}
    public String pretty_print() {
        String word_str = "   Boolean_literal" + Boolean;
        return word_str;
      }

}

class TrueExp extends BooleanExp
{
    TrueExp(String exp)
    {
        super(exp);
    }
}

class FalseExp extends BooleanExp
{
    FalseExp(String exp)
    {
        super(exp);
    }
}


class ArgumentList implements AST_Node
{
    private ArrayList<Expression> arg_list;

    public ArgumentList()
    {
        arg_list = new ArrayList<Expression>();
    }
    public void add(Expression arguement)
    {
        this.arg_list.add(0, arguement);
    }

    public int size(){return arg_list.size();}
    
    public Expression get(int i)
    {
        return this.arg_list.get(i);
    }

    public ArrayList<Expression> value()
    {
        return arg_list;
    }


    public String pretty_print() {
        String word_str = "";
        for (int i = 0; i < arg_list.size(); i++) {
          word_str += arg_list.get(i).pretty_print();
        }
        return word_str;
      }
    
      public String toString() {
        String word_str = "";
        for (int i = 0; i < arg_list.size(); i++) {
          word_str += arg_list.get(i).toString();
        }
        return word_str;
      }
      
}

class Stop implements AST_Node
{
    public Stop(){}
}