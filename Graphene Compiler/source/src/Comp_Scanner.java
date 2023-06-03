
package src;
import java.util.*;
public class Comp_Scanner {

    static final String EOF = "$";

   Map<String, ArrayList<String>> RESERVED_WORDS = new HashMap<String, ArrayList<String>>(){{
       put("IDENTIFIER", new ArrayList<String>(){{
           add("print");
       }});
       put("BOOLITERALS", new ArrayList<String>(){{
           add("true");
           add("false");
       }});
       put(("KEYWORDS"), new ArrayList<String>(){{
           add("integer");
           add("if");
           add("not");
           add("fn");
           add("boolean");
           add("else");
           add("and");
           add("or");
       }});
   }};

   Map<String, ArrayList<String>> SYMBOLS = new HashMap<String, ArrayList<String>>(){{

       put("PUNCTUATION", new ArrayList<String>(){{
           add("(");
           add(")");
           add(",");
           add(":");
           add("->");
       }});

       put("OPERATORS", new ArrayList<String>(){{
           add("+");
           add("-");
           add("*");
           add("/");
           add("<");
           add("==");
           add("=");
       }});

       put("SPECIAL_CHARACTERS", new ArrayList<String>(){{
           add("_");
           add("//");
       }});

   }};
      
   
   Map<String, ArrayList<String>> SKIPPABLE_CHARACTERS = new HashMap<String, ArrayList<String>>(){{
       put("WHITESPACE", new ArrayList<String>(){{
           add(" ");
           add("\t");
           add("\r");
           add("\n");
       }});
   }};

   Map<String, ArrayList<String>> INTEGER_LITERALS = new HashMap<String, ArrayList<String>>(){{
       put("INTEGER", new ArrayList<String>(){{
           add("0");
           add("1");
           add("2");
           add("3");
           add("4");
           add("5");
           add("6");
           add("7");
           add("8");
           add("9");
       }});
   }};

   Map<String, ArrayList<String>> ALL_TOKENS = new HashMap<String, ArrayList<String>>(){{
       put("keyword", new ArrayList<String>());
       put("identifier", new ArrayList<String>());
       put("integer", new ArrayList<String>());
       put("Boolean", new ArrayList<String>());
       put("left_paren", new ArrayList<String>());
       put("right_paren", new ArrayList<String>());
       put("returns", new ArrayList<String>());
       put("BOOLEAN_LITERAL", new ArrayList<String>());
       put("INTEGER_LITERAL", new ArrayList<String>());
       put("punc", new ArrayList<String>());
       put("operator", new ArrayList<>());
       put("$", new ArrayList<String>(){{
           add("end_of_file");
       }});
   }};



   private String input;
   private int position;
   private StringBuilder temp_alpha = new StringBuilder();
   private Character charPeek(){
    position++;
    return input.charAt(position);
   }
   ArrayList<String> accum = new ArrayList<String>();

   public Comp_Scanner(String input){
       this.input = input;
       position = 0;
   }

   /**
    * Description: Check if the current character is in the RESERVED_WORDS list
    * @param word
    * @return
    */
   public boolean isKeyword(String word){
       if (RESERVED_WORDS.get("KEYWORDS").contains(word)){
           return true;
       }
       return false;
   }

   /**
    * Description: Checks if the current character is a delimiter
    *              it checks it in the PUNCTUATION & WHITESPACE LIST
    * @param word
    * @return
    */
   public boolean isDelimiter(String word){
       if (SYMBOLS.get("PUNCTUATION").contains(word) || SKIPPABLE_CHARACTERS.get("WHITESPACE").contains(word)|| SYMBOLS.get("OPERATORS").contains(word)){
           return true;
       }
       return false;
   }

   /**
    * Requirements:
    *   1. A user-defined IDENTIFIER is a string beginning with a letter and consisting of letters, digits, and the underscore ( _ ). 
    *   2. Upper and lower-case letters are not considered equivalent. 
    *   3. Graphene identifiers are case-sensitive
    *   4. Identifiers must be no longer than 256 characters. 
    * @param word
    * @return boolean
    */
   public boolean isIdentifer(String word){
       if(!word.isEmpty()){
           if(word.length() > 256){
            throw new LexicalError("Identifier too long");
               //return false;
           }
           if (RESERVED_WORDS.get("IDENTIFIER").contains(word)){
               return true;
           }
           if (Character.isLetter(word.charAt(0))){
               for (int i = 1; i < word.length(); i++){
                   if (!Character.isLetterOrDigit(word.charAt(i)) && word.charAt(i) != '_'){
                    throw new LexicalError("invalid identifier. only use letter,digit and _");
                       //return false;
                   }
               }
               return true;
           }
           return false;
   }
   return false;
}

   /**
    * Requirements:
    *   1. An INTEGER-LITERAL is a string of digits. Leading zeros are not permitted for non-zero integer literals. 
    *   2. TODO: There are no leading plus or minus signs to indicate positive or negative values. Thus, all integer literals are positive 
    *   3. Integer literals must be in the range from 0 to 231-1, inclusive. 
    * @param word
    * @return boolean
    */
   public boolean isDigitT(String word) {
       if(!word.isEmpty()){
       try {

           //  check for leading zeros
           
           

               if (word.charAt(0) == '0' && word.length() > 1){

                       throw new LexicalError("Leading zeros are not allowed");
                       //return false;
                   
               }
               int i = Integer.parseInt(word);
               //if (i >= 0 && i > Math.pow(2, 31) - 1) {
               //     throw new LexicalError("number is too large");
                   //return true;
               //}
               if (word.charAt(0) == '-'){
                throw new LexicalError("Leading minus signs are not allowed");
               }
               else {
                //throw new LexicalError("number is too large");
                   return true;
               }
           
               
               
           }
           

       catch (NumberFormatException e) {
        throw new LexicalError("number is invalid");
       } 

       }
       
    return false;
}

   /**
    * Requirements:
    *  1. Comments and whitespace are ignored by the syntax. 
    *  2.  Whitespace consists only of space (" "), tab ("\t"), and end-of-line ("\n" and "\r") characters. 
    *       It serves to separate tokens. Whitespace characters may not appear inside a literal, identifier, 
    *       keyword, or operator. Otherwise, whitespace is insignificant. 
    *  3. A comment starts with the double-slash "//" and continues up to the next occurrence of an end-of-line character.
    * @param word
    * @return boolean
    */
   public boolean isSkipable(String word){
       if (SKIPPABLE_CHARACTERS.get("WHITESPACE").contains(word) || SYMBOLS.get("SPECIAL_CHARACTERS").contains(word)){
           return true;
       }
       return false;
   }
   public boolean isBoolean(String word){
       if (RESERVED_WORDS.get("BOOLITERALS").contains(word)){
           return true;
       }
       return false;
   }

   /**
    * @param word
    * @return boolean
    */
   public boolean isSymbol(String word){
       if (SYMBOLS.get("PUNCTUATION").contains(word) || SYMBOLS.get("OPERATORS").contains(word)){
           return true;
       }
       return false;
   }

   /**
    * 
    * @param word
    * @return
    */
   public boolean isPunct(String word){
       if (SYMBOLS.get("PUNCTUATION").contains(word)){
           return true;
       }
       return false;
    
   }

   public Token next() {
   // length with whitespace
   temp_alpha.setLength(0);
   
   
   while (position < input.length()) {
        char c = input.charAt(position);
        
           
           if (isDelimiter(Character.toString(c))) {
        
            
               if (isKeyword(temp_alpha.toString())){
                if(temp_alpha.toString().equals("integer")){
                    ALL_TOKENS.get("integer").add(temp_alpha.toString());
                   return new Token("integer", (temp_alpha.toString()));
                }
                else if(temp_alpha.toString().equals("boolean")){
                    ALL_TOKENS.get("Boolean").add(temp_alpha.toString());
                   return new Token("Boolean", (temp_alpha.toString()));
                }
                else{
                    ALL_TOKENS.get("keyword").add(temp_alpha.toString());
                   return new Token("keyword", (temp_alpha.toString()));
                }
                   
            }else if (isBoolean(temp_alpha.toString())){
                ALL_TOKENS.get("BOOLEAN_LITERAL").add(temp_alpha.toString());
                return new Token("BOOLEAN_LITERAL", (temp_alpha.toString()));
              }
                else if (isIdentifer(temp_alpha.toString())){
                   ALL_TOKENS.get("identifier").add(temp_alpha.toString());
                   return new Token("identifier", (temp_alpha.toString()));
              }

              if(isDigitT(temp_alpha.toString())) {
               ALL_TOKENS.get("INTEGER_LITERAL").add(temp_alpha.toString());
               return new Token("INTEGER_LITERAL", (temp_alpha.toString()));
           }

               if(isPunct(Character.toString(c))) {
                  if((Character.toString(c)).equals("(")){
                   ALL_TOKENS.get("left_paren").add(Character.toString(c));
                   position++;
                   return new Token("left_paren", (Character.toString(c)));
               }    
               if((Character.toString(c)).equals(")")){
                ALL_TOKENS.get("right_paren").add(Character.toString(c));
                position++;
                return new Token("right_paren", (Character.toString(c)));
            }
                  
                  else {
                    ALL_TOKENS.get("punc").add(Character.toString(c));
                   position++;
                   return new Token("punc", (Character.toString(c)));
               }
            }
               if(isPunct(temp_alpha.toString())){
                if((temp_alpha.toString()) == "("){
                    ALL_TOKENS.get("left_paren").add(temp_alpha.toString());
                    return new Token("left_paren", (temp_alpha.toString()));
                }
                if((temp_alpha.toString()) == ")"){
                    ALL_TOKENS.get("right_paren").add(temp_alpha.toString());
                    return new Token("right_paren", (temp_alpha.toString()));
                }
                else {                
                   ALL_TOKENS.get("punc").add(temp_alpha.toString());
                   return new Token("punc", (temp_alpha.toString()));
               }
            }
               if(isSymbol(Character.toString(c))){
                  temp_alpha.append(c);
                  if(Character.toString(charPeek()).equals("/")){
                  while(position < input.length() && input.charAt(position) != '\n')
                  {
                    position ++;
                  }
                  return (next());
                }
                position--;
                if(Character.toString(charPeek()).equals(">")){
                    position--;
                    temp_alpha.append(charPeek());
                    ALL_TOKENS.get("returns").add(temp_alpha.toString());
                    position++;
                   return new Token("returns", (temp_alpha.toString()));
                }
                position--;
                if(Character.toString(charPeek()).equals("=")){
                    position--;
                    temp_alpha.append(charPeek());
                    ALL_TOKENS.get("operator").add(temp_alpha.toString());
                    position++;
                   return new Token("operator", (temp_alpha.toString()));
                }
                position--;
                if(Character.toString(c) == null){

                }
                  else {
                    ALL_TOKENS.get("operator").add(Character.toString(c));
                    position++;
                   return new Token("operator", (Character.toString(c)));
                  }
                   
               }
               if(isSkipable(temp_alpha.toString())){
                   ALL_TOKENS.get("skippable").add(temp_alpha.toString());
                   return new Token("skippable", (temp_alpha.toString()));
               }   
           position++;
       } else {
           temp_alpha.append(c);
           position++;
           

       }
       
   
       }

    if (isKeyword(temp_alpha.toString())){
        if(temp_alpha.toString().equals("integer")){
            ALL_TOKENS.get("integer").add(temp_alpha.toString());
           return new Token("integer", (temp_alpha.toString()));
        }
        else if(temp_alpha.toString().equals("boolean")){
            ALL_TOKENS.get("Boolean").add(temp_alpha.toString());
           return new Token("Boolean", (temp_alpha.toString()));
        }
        else{
            ALL_TOKENS.get("keyword").add(temp_alpha.toString());
           return new Token("keyword", (temp_alpha.toString()));
        }
        
    }
    else if (isBoolean(temp_alpha.toString())){
     ALL_TOKENS.get("BOOLEAN_LITERAL").add(temp_alpha.toString());
     return new Token("BOOLEAN_LITERAL", (temp_alpha.toString()));
   }
     else if (isIdentifer(temp_alpha.toString())){
        ALL_TOKENS.get("identifier").add(temp_alpha.toString());
        return new Token("identifier", (temp_alpha.toString()));
   }

   if(isDigitT(temp_alpha.toString())) {
    //ALL_TOKENS.get("INTEGER_LITERAL").add(temp_alpha.toString());
    return new Token("INTEGER_LITERAL", (temp_alpha.toString()));
}

    if(isPunct(temp_alpha.toString())){
     if((temp_alpha.toString()) == "("){
         ALL_TOKENS.get("left_paren").add(temp_alpha.toString());
         return new Token("left_paren", (temp_alpha.toString()));
     }
     else {                
        ALL_TOKENS.get("right_paren").add(temp_alpha.toString());
        return new Token("right_paren", (temp_alpha.toString()));
    }
 }
    if(isSkipable(temp_alpha.toString())){
        ALL_TOKENS.get("skippable").add(temp_alpha.toString());
        return new Token("skippable", (temp_alpha.toString()));
    }




   
   position ++;
   return new Token(EOF , "");
}

   public boolean hasNext() {
       return position <= input.length();
   }
   // The peek function returns the next token but does not advance the pointer. 
   public Token peek() {
       int oldPosition = position;
       Token token = next();
       position = oldPosition;
       return token;
   }



}

class LexicalError extends RuntimeException{ 
    public LexicalError(String value_error) {
        super(value_error);
    }
}
//