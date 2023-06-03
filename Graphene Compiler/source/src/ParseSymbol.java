package src;
public enum ParseSymbol {
    // nonTerminals
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
    LITERAL,

// Terminals

    identifier,
    fn,
    integer,
    Boolean,
    BOOLEAN_LITERAL,
    INTEGER_LITERAL,
    Comma,
    Colon,
    Plus,
    Minus,
    Multiply,
    Division,
    Print,
    And,
    Or,
    Not,
    If,
    Else,
    Equal,
    Less_than,
    left_paren,
    right_paren,
    $,

    keyword,
    returns,
    punc,
    operator,
    
//keywords

//ds

    True,        
    False,

//Make Symbols

    MakeDefList,
    MakeDef,
    MakeBody,
    MakeIWT,
    MakeFrmlParam,
    MakeInt,
    MakeBool,
    MakePrint,
    MakeEquals,
    MakeLessThan,
    MakeOr,
    MakePlus,
    MakeMinus,
    MakeAnd,
    MakeTimes,
    MakeDivide,
    MakeNot,
    MakeNeg,
    MakeID,
    MakeStop,
    MakeIf,
    MakeThen,
    MakeElse,
    MakeFunct,
    MakeFrmlArgs,
    MakeBoolLit,
    MakeIntLit;

}
