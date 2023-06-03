package src;
import java.util.*;
public class Parser {



    /* private String file;

     public Parser(String file) {
         this.file = file;
         this.scanner = new Scanner(file);
     }
     */
    private Comp_Scanner scanner;
    private String matched_terminal;
    Stack < AST_Node > semantic_stack = new Stack < AST_Node > ();

    public Parser(Comp_Scanner scanner2) {
        scanner = scanner2;
    }


    private ParseSymbol getTokenSymbol(Token p){
        String type = p.getType();
        String value = p.getValue();
        if (type.equals("keyword")){
            if(value.equals("if")){
                return ParseSymbol.If;
            }
            if(value.equals("not")){
                return ParseSymbol.Not;
            }
            if(value.equals("fn")){
                return ParseSymbol.fn;
            }
            if(value.equals("else")){
                return ParseSymbol.Else;
            }
            if(value.equals("and")){
                return ParseSymbol.And;
            }
            if(value.equals("or")){
                return ParseSymbol.Or;
            }
        }

        if (type.equals("operator")){
            if(value.equals("+")){
                return ParseSymbol.Plus;
            }
            if(value.equals("-")){
                return ParseSymbol.Minus;
            }
            if(value.equals("*")){
                return ParseSymbol.Multiply;
            }
            if(value.equals("/")){
                return ParseSymbol.Division;
            }
            if(value.equals("==")){
                return ParseSymbol.Equal;
            }
            if(value.equals("<")){
                return ParseSymbol.Less_than;
            }
        }

        if(type.equals("identifier")){
            if(value.equals("print")){
                return ParseSymbol.Print;
            }
            return ParseSymbol.identifier;
        }
        if(type.equals("integer")){
            return ParseSymbol.integer;
        }
        if(type.equals("Boolean")){
            return ParseSymbol.Boolean;
        }

        if(type.equals("BOOLEAN_LITERAL")){
            if(value.equals("true")){
                return ParseSymbol.BOOLEAN_LITERAL;
            }
            if(value.equals("false")){
                return ParseSymbol.BOOLEAN_LITERAL;
            }
        }
        if (type.equals("INTEGER_LITERAL")){
            return ParseSymbol.INTEGER_LITERAL;
        }

        if(type.equals("punc")){
            if(value.equals(",")){
                return ParseSymbol.Comma;
            }
            if(value.equals(":")){
                return ParseSymbol.Colon;
            }
        }
        if(type.equals("returns")){
            return ParseSymbol.returns;
        }

        if(type.equals("left_paren")){
            return ParseSymbol.left_paren;
        }

        if(type.equals("right_paren")){
            return ParseSymbol.right_paren;
        }
        return ParseSymbol.$;

    }



    private EnumMap < ParseSymbol, EnumMap < ParseSymbol, ParseSymbol[]>> parseTable =
            new EnumMap < ParseSymbol, EnumMap < ParseSymbol, ParseSymbol[]>>(ParseSymbol.class) {
                {
                    // first row
                    put( ParseSymbol.DEFINITION_LIST, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.fn, new ParseSymbol[]{
                                    ParseSymbol.DEFINITION,
                                    ParseSymbol.MakeDef,
                                    ParseSymbol.DEFINITION_LIST
                            });
                            put( ParseSymbol.$, new ParseSymbol[]{
                                    //ParseSymbol.EPSILON
                            });
                        }
                    });

                    // second row

                    put( ParseSymbol.DEFINITION, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.fn, new ParseSymbol[]{
                                    ParseSymbol.fn,
                                    ParseSymbol.identifier,
                                    ParseSymbol.MakeID,
                                    ParseSymbol.left_paren,
                                    ParseSymbol.PARAMETER_LIST,
                                    ParseSymbol.MakeFrmlParam,
                                    ParseSymbol.right_paren,
                                    ParseSymbol.returns,
                                    ParseSymbol.TYPE,
                                    ParseSymbol.BODY,
                                    ParseSymbol.MakeBody
                            });
                        }
                    });
                    // third row
                    put( ParseSymbol.PARAMETER_LIST, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_PARAMETERS

                            });
                            put( ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.EPSILON
                            });
                        }
                    });

                    // fourth row
                    put( ParseSymbol.FORMAL_PARAMETERS, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.ID_WITH_TYPE,
                                    ParseSymbol.MakeIWT,
                                    ParseSymbol.ID_WITH_TYPE_p
                            });
                        }
                    });

                    // fifth row
                    put( ParseSymbol.ID_WITH_TYPE, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.identifier,
                                    ParseSymbol.MakeID,
                                    ParseSymbol.Colon,
                                    ParseSymbol.TYPE
                            });
                        }
                    });
                    // sixth row
                    put( ParseSymbol.ID_WITH_TYPE_p, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.Comma, new ParseSymbol[]{
                                    ParseSymbol.Comma,
                                    ParseSymbol.FORMAL_PARAMETERS,
                                    //ParseSymbol.MakeFrmlParam
                            });
                            put( ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.EPSILON
                            });
                        }
                    });
                    // seventh row
                    put( ParseSymbol.TYPE, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.integer, new ParseSymbol[]{
                                    ParseSymbol.integer,
                                    ParseSymbol.MakeInt
                            });
                            put( ParseSymbol.Boolean, new ParseSymbol[]{
                                    ParseSymbol.Boolean,
                                    ParseSymbol.MakeBool
                            });
                        }
                    });

                    // eight row

                    put( ParseSymbol.BODY, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                            });
                            put( ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION
                            });
                            put( ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION
                            });
                            put( ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION
                            });
                            put( ParseSymbol.Print, new ParseSymbol[]{
                                    ParseSymbol.PRINT_EXPRESSION,
                                    ParseSymbol.BODY
                            });
                            put( ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION
                            });
                            put( ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION
                            });
                            put( ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION
                            });
                        }
                    });

                    // ninth row

                    put( ParseSymbol.PRINT_EXPRESSION, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.Print, new ParseSymbol[]{
                                    ParseSymbol.Print,
                                    ParseSymbol.MakeID,
                                    ParseSymbol.left_paren,
                                    ParseSymbol.MakeStop,
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.right_paren,
                                    ParseSymbol.MakeFrmlArgs,
                                    ParseSymbol.MakeFunct
                            });
                        }
                    });
                    // tenth row

                    put( ParseSymbol.EXPRESSION, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.Plus, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });// not sure added while debugging
                            put(ParseSymbol.Or, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.Equal, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });
                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.SIMPLE_EXPRESSION_p,
                            });// added while debugging not sure

                        }
                    });

                    // eleventh row

                    put( ParseSymbol.EXPRESSION_p, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.Comma, new ParseSymbol[]{
                                    ParseSymbol.Comma,
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    //ParseSymbol.MakeFrmlArgs,
                            });
                            put(ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                                    ParseSymbol.MakeFrmlArgs,
                            });
                        }
                    });

                    //twelve row

                    put( ParseSymbol.SIMPLE_EXPRESSION, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.Less_than, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });
                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.TERM,
                                    ParseSymbol.TERM_p
                            });// added while debugging not sure
                            //confusion for less than
                        }
                    });

                    // thirteen row
                    put( ParseSymbol.SIMPLE_EXPRESSION_p, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.fn, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon,
                            });
                            put(ParseSymbol.Comma, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Plus, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });

                            //confusion for minus

                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Multiply, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Division, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.And, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Or, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Else, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Equal, new ParseSymbol[]{
                                    ParseSymbol.Equal,
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.MakeEquals
                            });
                            put(ParseSymbol.Less_than, new ParseSymbol[]{
                                    ParseSymbol.Less_than,
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.MakeLessThan
                            });
                            put(ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon,
                            });
                            put(ParseSymbol.$, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon,
                            });
                            //confusion for right_paren and $

                        }
                    });


                    // fourteen row

                    put( ParseSymbol.TERM, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                            put(ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                            put(ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.FACTOR_p
                            });
                        }
                    });

                    // fifteen row

                    put( ParseSymbol.TERM_p, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.fn, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Comma, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Plus, new ParseSymbol[]{
                                    ParseSymbol.Plus,
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.MakePlus
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.Minus,
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.MakeMinus
                            });
                            put(ParseSymbol.Multiply, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Division, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.And, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Or, new ParseSymbol[]{
                                    ParseSymbol.Or,
                                    ParseSymbol.SIMPLE_EXPRESSION,
                                    ParseSymbol.MakeOr
                            });
                            put(ParseSymbol.Else, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Equal, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Less_than, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.$, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                        }
                    });

                    // sixteen row

                    put( ParseSymbol.FACTOR, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.identifier,
                                    ParseSymbol.MakeID,
                                    ParseSymbol.IDENTIFIER
                                    //confusion check parse table
                            });
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.LITERAL
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.LITERAL
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.Minus,
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.MakeNeg
                            });
                            put(ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.Not,
                                    ParseSymbol.FACTOR,
                                    ParseSymbol.MakeNot
                            });
                            put(ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.If,
                                    ParseSymbol.left_paren,
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.right_paren,
                                    //ParseSymbol.MakeThen,
                                    ParseSymbol.EXPRESSION,
                                    //ParseSymbol.MakeElse,
                                    ParseSymbol.Else,
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.MakeIf
                            });
                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.left_paren,
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.right_paren
                            });

                        }
                    });

                    // seventeenth row

                    put( ParseSymbol.FACTOR_p, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.fn, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Comma, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Plus, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Multiply, new ParseSymbol[]{
                                    ParseSymbol.Multiply,
                                    ParseSymbol.TERM,
                                    ParseSymbol.MakeTimes
                            });
                            put(ParseSymbol.Division, new ParseSymbol[]{
                                    ParseSymbol.Division,
                                    ParseSymbol.TERM,
                                    ParseSymbol.MakeDivide
                            });
                            put(ParseSymbol.And, new ParseSymbol[]{
                                    ParseSymbol.And,
                                    ParseSymbol.TERM,
                                    ParseSymbol.MakeAnd
                            });
                            put(ParseSymbol.Or, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Else, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Equal, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Less_than, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.$, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                        }
                    });

                    put( ParseSymbol.IDENTIFIER, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.fn, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Comma, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Plus, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Multiply, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Division, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.And, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Or, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Else, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Equal, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.Less_than, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.left_paren,
                                    ParseSymbol.MakeStop,
                                    ParseSymbol.ARGUMENT_LIST,
                                    ParseSymbol.right_paren
                            });
                            put(ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                            put(ParseSymbol.$, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                        }
                    });

                    // eighteen row

                    put( ParseSymbol.ARGUMENT_LIST, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });
                            put(ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });
                            put(ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });

                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.FORMAL_ARGUMENTS,
                                    ParseSymbol.MakeFunct
                            });
                            put(ParseSymbol.right_paren, new ParseSymbol[]{
                                    //ParseSymbol.Epsilon
                            });
                        }
                    });

                    // nineteen row
                    put( ParseSymbol.FORMAL_ARGUMENTS, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.identifier, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });
                            put(ParseSymbol.Minus, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });
                            put(ParseSymbol.Not, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });
                            put(ParseSymbol.If, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });

                            put(ParseSymbol.left_paren, new ParseSymbol[]{
                                    ParseSymbol.EXPRESSION,
                                    ParseSymbol.EXPRESSION_p
                            });

                    /*put(ParseSymbol.right_paren, new ParseSymbol[]{
                        //ParseSymbol.Epsilon
                    });
                     Confusion match previous and current parse table*/
                        }
                    });

                    // twenty row

                    put( ParseSymbol.LITERAL, new EnumMap < ParseSymbol, ParseSymbol[]> (ParseSymbol.class) {
                        {
                            put(ParseSymbol.BOOLEAN_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.BOOLEAN_LITERAL,
                                    ParseSymbol.MakeBoolLit
                            });
                            put(ParseSymbol.INTEGER_LITERAL, new ParseSymbol[]{
                                    ParseSymbol.INTEGER_LITERAL,
                                    ParseSymbol.MakeIntLit
                            });
                        }
                    });


                }
            };


    public void printParseTable()
    {
        for (ParseSymbol key: parseTable.keySet())
        {
            System.out.println(key + " " + parseTable.get(key));
        }
    }



    public Boolean parse() {
        Stack < ParseSymbol > stack = new Stack < ParseSymbol > ();
        //Stack < AST_Node > semantic_stack = new Stack < AST_Node > ();
        stack.push(ParseSymbol.$);
        stack.push(ParseSymbol.MakeDefList);
        stack.push(ParseSymbol.DEFINITION_LIST);
        ParseSymbol top = stack.peek();
        //System.out.println(Terminals.values());
        //printParseTable();

        while (top != ParseSymbol.$) {
             /*for (Object key: stack) {
                System.out.println(key);
            }
            */

            if (Terminals.contains(top)){
                Token token = scanner.peek();
                //System.out.println(token);
                if (top == getTokenSymbol(token)) {
                    stack.pop();
                    matched_terminal = scanner.peek().getValue();
                } else {
                    System.out.println("parse error");
                    return false;
                }
                token = scanner.next();

            } else if (nonTerminals.contains(top)){
                Token tokenp = scanner.peek();
                //System.out.println(tokenp);
                EnumMap<ParseSymbol, ParseSymbol[]> row = parseTable.get(top);
                //System.out.println(row);
                if (row == null){
                    System.out.println("parse symbol not present");
                }
                ParseSymbol[] production = row.get(getTokenSymbol(tokenp));//parseTable.get(top).get(getTokenSymbol(tokenp));
                //System.out.println(Arrays.toString(production));
                if (parseTable.get(top).containsKey(getTokenSymbol(tokenp))){
                    stack.pop();
                    //System.out.println(production.length);
                    for (int i = production.length - 1; i >= 0; i--) {
                        stack.push(production[i]);

                    }
                }
                else {
                    System.out.println("parse error");
                    return false;
                }
            } else if(MakeSymbol.contains(top)){
                switch(top){
                    case MakeDefList:{
                        //for (Object key: semantic_stack) {
                        //   System.out.println(key);
                        //}
                        Definition_List func_list = new Definition_List();
                        while (semantic_stack.size() != 0){
                            func_list.add((DefinitionNode)semantic_stack.peek());
                            semantic_stack.pop();
                        }
                        semantic_stack.push(func_list);
                        stack.pop();

                        break;
                    }
                    case MakeDef:{

                        Body body = (Body)semantic_stack.peek();
                        semantic_stack.pop();

                        TypeNode type = (TypeNode)semantic_stack.peek();
                        semantic_stack.pop();

                        ParameterList list =(ParameterList)semantic_stack.peek();
                        semantic_stack.pop();

                        IdentifierExp id = (IdentifierExp)semantic_stack.peek();
                        semantic_stack.pop();

                        DefinitionNode def = new DefinitionNode(id, list, type, body);

                        semantic_stack.push(def);
                        stack.pop();

                        //System.out.println(def.pretty_print());

                        break;

                    }
                    case MakeBody:{
                        ArrayList<Expression> body = new ArrayList<Expression>();
                        while (semantic_stack.peek() instanceof Expression){
                            body.add((Expression)semantic_stack.peek());
                            semantic_stack.pop();
                        }
                        Collections.reverse(body);
                        semantic_stack.push(new Body(body));
                        stack.pop();

                        break;
                    }

                    case MakeIWT:{
                        TypeNode type = (TypeNode)semantic_stack.peek();
                        semantic_stack.pop();

                        IdentifierExp id = (IdentifierExp)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new IdWithType(id, type));
                        stack.pop();
                        break;
                    }

                    case MakeFrmlParam:{

                        ParameterList list = new ParameterList();
                        while(semantic_stack.peek() instanceof IdWithType){
                            list.add((IdWithType)semantic_stack.peek());
                            semantic_stack.pop();
                        }

                        semantic_stack.push(list);
                        stack.pop();
                        break;
                    }
                    case MakeInt:{
                        semantic_stack.push(new TypeNode(TypeNode.Type.IntegerType));
                        stack.pop();

                        break;
                    }
                    case MakeBool:{
                        semantic_stack.push(new TypeNode(TypeNode.Type.BooleanType));
                        stack.pop();

                        break;
                    }
                    /* 
                    case MakePrint:{
                        FunctionCall exp = (FunctionCall)semantic_stack.peek();
                        semantic_stack.pop();
                        semantic_stack.push(new print_expression(exp));
                        stack.pop();

                        break;
                    }
                    */
                    case MakeEquals:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new EqualityExp(left, right));
                        stack.pop();

                        break;
                    }
                    case MakeLessThan:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new LessThanExp(left, right));
                        stack.pop();

                        break;
                    }

                    case MakeOr:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new OrExp(left, right));
                        stack.pop();

                        break;
                    }

                    case MakePlus:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new PlusExp(left, right));
                        stack.pop();

                        break;
                    }

                    case MakeMinus:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new MinusExp(left, right));
                        stack.pop();

                        break;
                    }

                    case MakeAnd:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new AndExp(left, right));
                        stack.pop();

                        break;
                    }

                    case MakeTimes:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new TimesExp(left, right));
                        stack.pop();

                        break;
                    }

                    case MakeDivide:{
                        Expression right = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression left = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new DivideExp(left, right));
                        stack.pop();

                        break;
                    }
                    case MakeNot:{
                        Expression unar = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new NotExp(unar));
                        stack.pop();
                        break;
                    }

                    case MakeNeg:{
                        Expression unar = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new NegateExp(unar));
                        stack.pop();
                        break;
                    }
                    case MakeID:{
                        semantic_stack.push(new IdentifierExp(matched_terminal));
                        stack.pop();
                        break;
                    }


                    case MakeFunct:{
                        ArgumentList list = (ArgumentList)semantic_stack.peek();
                        semantic_stack.pop();

                        IdentifierExp id = (IdentifierExp)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new FunctionCall(id, list));
                        stack.pop();

                        break;
                        
                    }
                    
                    case MakeStop:{
                        //IdentifierExp id = (IdentifierExp)semantic_stack.peek();
                        //semantic_stack.pop();

                        semantic_stack.push(new Stop());
                        stack.pop();

                        break;
                    }
                    

                    case MakeFrmlArgs:{
                        ArgumentList list = new ArgumentList();
                        while(semantic_stack.peek() instanceof Expression){
                            list.add((Expression)semantic_stack.peek());
                            semantic_stack.pop();
                            
                        }
                        semantic_stack.pop();
                        semantic_stack.push(list);
                        stack.pop();
                        break;
                    }
                    

                    case MakeIf:{
                        Expression else1 = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression then = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        Expression cond = (Expression)semantic_stack.peek();
                        semantic_stack.pop();

                        semantic_stack.push(new IfExp(cond, then, else1));
                        stack.pop();

                        break;

                    }
                    case MakeIntLit:{
                        semantic_stack.push(new NumberExp(matched_terminal));
                        stack.pop();
                        break;
                    }

                    case MakeBoolLit:{
                        String data = matched_terminal;
                        if (data == "true"){
                            semantic_stack.push(new TrueExp(data));
                        }
                        else{
                            semantic_stack.push(new FalseExp(data));
                        }

                        stack.pop();
                        break;
                    }
                    default:
                        break;



                }

            }
            else {
                System.out.println("Symbol Error");
                return false;
            }
            top = stack.peek();
        }
        return true;



    }

}


