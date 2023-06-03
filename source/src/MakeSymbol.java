package src;

public enum MakeSymbol 
{
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


    public static boolean contains(ParseSymbol top) {
        for(MakeSymbol AST: MakeSymbol.values()) {
            if(AST.toString() == top.toString()){
                return true;
            }
        }
        return false;
    }

}
