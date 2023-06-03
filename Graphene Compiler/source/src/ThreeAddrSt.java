
package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class ThreeAddrSt {
    Map<Integer,String> func_temp =new HashMap<Integer,String>();
    ArrayList<ThreeStType> All_Satement = new ArrayList<ThreeStType>();
    static int temp = 1;
    static int label = 1;
    String arg_loc;
    String rtn;
    private String MakenewTemp(){
        String tem = "t";
        tem = tem+String.valueOf(temp);
        return tem;
    }
    private String Makenewlabel(){
        String labels = "L";
        labels = labels+String.valueOf(label);
        return labels;
    }
    ThreeAddrSt (Expression exp){
        rtn = generate(exp);
    }
    
    private String generate(Expression exp){
        ThreeStType statement = new ThreeStType();
         if(exp instanceof BinaryExp){
            if (exp instanceof MinusExp){
                statement.type = ThreeStType.Type.Minus;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof PlusExp){
                statement.type = ThreeStType.Type.Plus;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof TimesExp){
                statement.type = ThreeStType.Type.Times;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof DivideExp){
                statement.type = ThreeStType.Type.Divide;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof LessThanExp){
                statement.type = ThreeStType.Type.LessThan;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof EqualityExp){
                statement.type = ThreeStType.Type.Equals;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof OrExp){
                statement.type = ThreeStType.Type.Or;
                statement.category = ThreeStType.Category.binaryGen;
            }
            else if(exp instanceof AndExp){
                statement.type = ThreeStType.Type.And;
                statement.category = ThreeStType.Category.binaryGen;
            }
            //store value from left and right of binary then perform the operation 
            statement.temp = MakenewTemp();
            temp++;
            statement.left_child = generate(((BinaryExp)exp).getleft());
            statement.right_child = generate(((BinaryExp)exp).getright());
            All_Satement.add(statement);

            return statement.temp;
        } 
        else if(exp instanceof UnaryExp){
            if (exp instanceof NegateExp){
                statement.type = ThreeStType.Type.Negate;
                statement.category = ThreeStType.Category.unaryGen;
            }
            else if(exp instanceof NotExp){
                statement.type = ThreeStType.Type.Not;
                statement.category = ThreeStType.Category.unaryGen;
            }
            statement.temp = MakenewTemp();
            temp++;
            statement.right_child = generate(((UnaryExp)exp).getExp());
            All_Satement.add(statement);
            return statement.temp;
        }
        else if(exp instanceof NumberExp){
            statement.type = ThreeStType.Type.Store;
            statement.category = ThreeStType.Category.storageGen;
            statement.temp = MakenewTemp();
            temp++;
            statement.left_child = ((NumberExp)exp).getnum();
            All_Satement.add(statement);

            return statement.temp;
        }
        else if(exp instanceof BooleanExp){
            statement.type = ThreeStType.Type.Store;
            statement.category = ThreeStType.Category.storageGen;
            statement.temp = MakenewTemp();
            temp++;
            String bool = "";
            if (((BooleanExp)exp).getBool() == "true" ){
                bool = "1";
            }
            else {
                bool = "0";
            }
            statement.left_child = bool;
            All_Satement.add(statement);

            return statement.temp;
        }
        else if(exp instanceof IdentifierExp){
            statement.type = ThreeStType.Type.Store;
            statement.category = ThreeStType.Category.storageGen;
            statement.temp = MakenewTemp();
            temp++;
            statement.left_child = ((IdentifierExp)exp).getid();
            All_Satement.add(statement);

            return statement.temp;
        }
         
        else if(exp instanceof IfExp){
            /* 
            if (a < b) a else b
            t1 := a
            t2 := b
            t3 := a < b
            IF_NOT t3 GOTO L1
            t4 := a
            GOTO L2
            LABEL L1
            t4 := b
            LABEL L2
             */
                   

           String temp = MakenewTemp();
           //labels
           String label_1 = Makenewlabel();
           label ++;
           String label_2 = Makenewlabel();
           label ++;

           //if statement
           //statement = new ThreeStType();
           statement.type = ThreeStType.Type.If_start;
           statement.category = ThreeStType.Category.ifGen;
           statement.left_child = generate(((IfExp)exp).condition());
           statement.right_child = label_1;
           //statement.temp = L;
           All_Satement.add(statement);

           //then_Clause
           statement =  new ThreeStType();
           statement.temp = temp;
           statement.type = ThreeStType.Type.Str_if;
           statement.category = ThreeStType.Category.ifGen;
           statement.left_child = generate(((IfExp)exp).thenClause());
           All_Satement.add(statement);

           //GOTO label 2 

           statement = new ThreeStType();
           statement.type = ThreeStType.Type.Goto;
           statement.category = ThreeStType.Category.ifGen;
           statement.left_child = label_2;
           All_Satement.add(statement);


           //label 1 

           statement = new ThreeStType();
           statement.type = ThreeStType.Type.Label;
           statement.category = ThreeStType.Category.ifGen;
           statement.left_child = label_1;
           All_Satement.add(statement);


           //else clause
           statement = new ThreeStType();
           statement.temp = temp;
           statement.type = ThreeStType.Type.Str_if;
           statement.category = ThreeStType.Category.ifGen;
           statement.left_child = generate(((IfExp)exp).elseClause());
           All_Satement.add(statement);

           //Label 2 
           statement = new ThreeStType();
           statement.type = ThreeStType.Type.Label;
           statement.category = ThreeStType.Category.ifGen;
           statement.left_child = label_2;
           All_Satement.add(statement);
            // if statement end
           statement = new ThreeStType();
           statement.type = ThreeStType.Type.if_end;
           statement.category = ThreeStType.Category.ifGen;
           All_Satement.add(statement);


           return temp;
        }
        else if(exp instanceof FunctionCall){
        
             /* 
             
             func_call = rem(n-1, n*acc)

            t1 := 1
            t2 := n - t1
            t3 := n * acc
            PARAM t2
            PARAM t3
            t4 := CALL rem 2
             */
             
             
            int arg_size = ((FunctionCall)exp).args().size();

            for (int i=0; i<arg_size; i++){
                arg_loc = generate(((FunctionCall)exp).args().get(i));
                func_temp.put(i,arg_loc);
            }

            statement.type = ThreeStType.Type.Func_St;
            statement.category = ThreeStType.Category.functionCallGen;
            statement.temp = ((FunctionCall)exp).id().getid();
            All_Satement.add(statement);
        

            for (int i = 0; i<arg_size; i++){
                statement = new ThreeStType();
                statement.type = ThreeStType.Type.PARAM;
                statement.category = ThreeStType.Category.functionCallGen; 
                statement.left_child = func_temp.get(i);
                All_Satement.add(statement);
            }

            statement = new ThreeStType();
            statement.type = ThreeStType.Type.Call;
            statement.temp = MakenewTemp();
            statement.category = ThreeStType.Category.functionCallGen;
            temp++;
            statement.right_child = ((FunctionCall)exp).id().getid();
            statement.left_child = String.valueOf(arg_size);
            All_Satement.add(statement);

            statement = new ThreeStType();
            statement.type = ThreeStType.Type.Func_end;
            statement.category = ThreeStType.Category.functionCallGen;
            All_Satement.add(statement);

            return statement.temp;
        }

        return statement.temp;
    }
}
