package src;
import java.io.*;

class Program {
    public static void main(String[] args) throws IOException {

       if (0 < args.length){
            File File = new File(args[0]);

            String file = "";
            int nextChar;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(File));
            while ((nextChar = reader.read()) != -1) {
                file += (char)nextChar + "";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. Create a scanner
       if (args[1].equals("Scanner")){
            All_classes.Scanner(file);
        //AST(file);
        }
       else if (args[1].equals("Parser")){
            All_classes.Parser(file);
       }
       else if (args[1].equals("AST")){
            All_classes.AST(file);
       }
       else if(args[1].equals("ThreeAddr")){
            All_classes.ThreeAddr(file);
       }
       else if(args[1].equals("Generator")){
            All_classes.CodeGen(file);
       }
    } 
    else {
        System.err.println("Filename is not provided");
        System.exit(0);
    }     
    
}
 
}



