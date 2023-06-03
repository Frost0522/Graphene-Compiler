package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class All_classes {
    String file;

    All_classes(String file) {
        this.file = file;
    }

    public static void Scanner(String file) {
        Comp_Scanner scanner = new Comp_Scanner(file);

        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }
    }

    public static void Parser(String file) {
        Comp_Scanner scanner = new Comp_Scanner(file);
        Parser parser = new Parser(scanner);

        if (parser.parse() == true) {
            System.out.println("valid program");
        } else {
            System.out.println(("invalid program"));
        }

    }

    public static void AST(String file) {
        Comp_Scanner scanner = new Comp_Scanner(file);
        Parser parser = new Parser(scanner);
        parser.parse();
        Definition_List ast = (Definition_List) parser.semantic_stack.peek();

        System.out.println("Program");
        // System.out.println(ast.pretty_print());
        for (DefinitionNode def_list : ast.value()) {
            System.out.println("    name " + def_list.id().getid());
            System.out.println("    params");
            for (IdWithType itp : def_list.pl().value()) {
                System.out.println("       " + itp.id().getid() + " : " + itp.type().Value());
            }
            System.out.println("    returns " + def_list.type().Value());
            System.out.println("    body");
            printbody printbody = new printbody(null);
            for (Expression exp : def_list.body().exp()) {
                printbody.print(exp);
            }
        }
    }

    public static void ThreeAddr(String file) {
        Comp_Scanner scanner = new Comp_Scanner(file);
        Parser parser = new Parser(scanner);
        parser.parse();
        Definition_List ast = (Definition_List) parser.semantic_stack.peek();

        for (DefinitionNode func_def : ast.value()) {
            System.out.println(func_def.id().getid() + " {");

            for (Expression exp : func_def.body().exp()) {
                ThreeAddrSt emitcode = new ThreeAddrSt(exp);

                for (ThreeStType statement : emitcode.All_Satement) {
                    String assign;
                    String left;
                    String right;
                    String type;
                    if (statement.temp != null) {
                        assign = statement.temp;
                    } else {
                        assign = "**";
                    }
                    if (statement.left_child != null) {
                        left = statement.left_child;
                    } else {
                        left = "**";
                    }
                    if (statement.right_child != null) {
                        right = statement.right_child;
                    } else {
                        right = "**";
                    }
                    if (statement.type != null) {
                        type = statement.type.toString();
                    } else {
                        type = "**";
                    }
                    System.out.println(assign + ": " + left + " " + type + " " + right);

                }
                ThreeAddrSt.temp = 0;
            }
            System.out.println("}\n");
        }
    }

    public static void CodeGen(String file) throws IOException {
        Comp_Scanner scanner = new Comp_Scanner(file);
        Parser parser = new Parser(scanner);
        parser.parse();
        Definition_List ast = (Definition_List) parser.semantic_stack.peek();

        Generator code_gen = new Generator(ast, "print-one.gr");
        code_gen.generate_program();

    }

}
