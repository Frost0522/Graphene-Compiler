package src;

public enum nonTerminals {

    DEFINITION_LIST,
    DEFINITION,
    PARAMETER_LIST,
    FORMAL_PARAMETERS,
    ID_WITH_TYPE,
    ID_WITH_TYPE_p,
    TYPE,
    BODY,
    PRINT_EXPRESSION,
    EXPRESSION,
    EXPRESSION_p,
    SIMPLE_EXPRESSION,

    SIMPLE_EXPRESSION_p,
    TERM,
    TERM_p,
    FACTOR,
    FACTOR_p,
    IDENTIFIER,
    ARGUMENT_LIST,
    FORMAL_ARGUMENTS,
    LITERAL;

    
    public static boolean contains(ParseSymbol top) {
        for(nonTerminals nT: nonTerminals.values()) {
            if(nT.toString() == top.toString()){
                return true;
            }
        }
        return false;
    }





}
