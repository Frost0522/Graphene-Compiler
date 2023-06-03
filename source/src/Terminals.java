package src;

public enum Terminals {
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
    returns,
    $;     

    //vf

    public static boolean contains(ParseSymbol top) {
        for(Terminals T: Terminals.values()) {
            if(T.toString() == top.toString()){
                return true;
            }
        }
        return false;
    }


   
}