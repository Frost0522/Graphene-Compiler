package src;



class ThreeStType {
    enum Type {
       
        Negate,
        Not,
        Call,
        Plus,
        Minus,
        Times,
        Divide,
        Equals,
        LessThan,
        Arg,
        PARAM,
        Or,
        And,
        Store,
        Func_St,
        Func_end,
        Str_if,
        IF_NOT,
        If_start,
        RETURN,
        Label,
        Goto,
        if_end
    }

    enum Category{
        binaryGen,
        unaryGen,
        booleanGen,
        formalGen,
        numberGen,
        exprGen,
        printGen,
        identiferGen,
        ifGen,
        actualsGen,
        storageGen,
        functionCallGen
    }

    
    Type type;
    String temp;
    String left_child;
    String right_child;
    Category category;
    
}
