package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import src.ThreeStType.Category;

class Generator {
    Definition_List ast;
    String File;
    static FileWriter FWriter;
    static int linenum = 0;

    public Generator(Definition_List ast, String file) {
        this.ast = ast;
        this.File = file;

        // creating TM file

        try {
            String tmFile = file.replace(".gr", ".tm");
            FWriter = new FileWriter(tmFile);
        } catch (IOException e) {
            System.out.println("unable to create: " + file);
        }
    }

    HashMap<String, Integer> func_Addr = new HashMap<String, Integer>();

    // Use a HashMap with the function name as the key and a FunctionInfo object as
    // the value
    HashMap<String, Integer> func_symbol_args = new HashMap<String, Integer>();

    // add function temporary
    HashMap<String, Integer> func_symbol_args_temp = new HashMap<String, Integer>();

    // function arguments lookup (hashmap inside hashmap)
    HashMap<String, HashMap<String, Integer>> function_arg_lookup = new HashMap<String, HashMap<String, Integer>>();

    HashMap<String, ArrayList<ThreeAddrSt>> func_three_code = new HashMap<String, ArrayList<ThreeAddrSt>>();
    HashMap<String, Integer> branch_function = new HashMap<String, Integer>();

    public void generateTM() throws IOException {
        generate_program();
    }

    public void generate_program() throws IOException {
        symbol_table(ast);
        generatePrologue();
        generate_functions();
    }

    // -----------------------------------------
    // function: generate prologue
    // -----------------------------------------
    public void generatePrologue() {
        int work_register = 1;
        emitCommentBlock("prologue");
        initialize_stack_pointers();
        call_main(work_register);
        print_result(work_register);
        halt_program();

    }

    public void initialize_stack_pointers() {
        emitComment("initialize top, status ptrs");
        emit_RM_instr(linenum, "LDC", 5, -1, 0, "initialize status ptr");
        emit_RM_instr(linenum + 1, "LDC", 6, 0, 0, "initialize top ptr");
        linenum += 2;
    }

    public void call_main(int work_register) {
        emitComment("call main()");
        int num_args = func_symbol_args.get("main");
        int num_temp = func_symbol_args_temp.get("main");
        int record_size = 8 + num_args + num_temp;

        gen_call_main(num_args, record_size);
        branch_To_Function("main");
        gen_return_from_main(work_register);
    }

    public void gen_call_main(int num_args, int record_size) {
        emit_RO_instr(linenum, "LDA", 1, 6, 7, "store");
        emit_RM_instr(linenum + 1, "ST", 1, (1 + num_args), 6, "return address");
        emit_RM_instr(linenum + 2, "ST", 5, (6 + num_args), 6, "save and set");
        emit_RM_instr(linenum + 3, "LDA", 5, (1 + num_args), 6, "status ptr");
        emit_RM_instr(linenum + 4, "ST", 6, (7 + num_args), 6, "save and set");
        emit_RM_instr(linenum + 5, "LDA", 6, record_size, 6, "top pointer");

        linenum += 6;
    }

    public void gen_return_from_main(int work_register) {
        emit_RM_instr(linenum, "LD", 6, 6, 5, "restore top ptr");
        emit_RM_instr(linenum + 1, "LD", 5, 5, 5, "restore status ptr");
        emit_RM_instr(linenum + 2, "LD", work_register, 0, 6, "set r1 to main() return value");
        linenum += 3;
    }

    public void print_result(int work_register) {
        emitComment("print value of main()");
        emit_RO_instr(linenum, "OUT", 1, 0, 0, "print r1");
        linenum++;
    }

    public void halt_program() {
        emit_RO_instr(linenum, "HALT", 0, 0, 0, "HALT");
        linenum++;
        emitCommentBlock("");
    }

    public void branch_To_Function(String funcName) {
        emitComment(linenum + ": branch to " + funcName);
        branch_function.put(funcName, linenum);
        linenum++;
    }

    public void generateBranch(String func_Name) {
        linenum = branch_function.get(func_Name);
        emit_RM_instr(linenum, "LDC", 7, func_Addr.get(func_Name), 0, "branch to " + func_Name);
        linenum++;
    }

    public void generate_functions() throws IOException {

        for (Map.Entry<String, Integer> functionsName : func_symbol_args.entrySet()) {
            generate_function(functionsName.getKey());
        }

        for (Map.Entry<String, Integer> function : branch_function.entrySet()) {
            generateBranch(function.getKey());
        }

    }

    public void generate_function(String function) throws IOException {
        func_Addr.put(function, linenum);
        int num_args = func_symbol_args.get(function);
        emitComment("* ---" + function + " function");
        saveRegisters(1, 4);
        emitComment("body");

        ArrayList<ThreeAddrSt> All_Statement = func_three_code.get(function);
        for (ThreeAddrSt statement : All_Statement) {
            generate_exprssion(statement, function);
        }
        String rtn = All_Statement.get(All_Statement.size() - 1).rtn.substring(1);
        emitComment("*Return" + rtn);
        emit_RM_instr(linenum, "LD", 1, -(Integer.valueOf(rtn)), 6, "r1" + rtn);
        emit_RM_instr(linenum + 1, "ST", 1, (-(num_args + 1)), 5, "returning r1");
        linenum += 2;
        emitComment("end");
        restoreRegisters(1, 4);
        emit_RM_instr(linenum, "LD", 7, 0, 5, "calling seq: return");
        linenum++;
    }


    public void generate_exprssion(ThreeAddrSt code, String funName) throws IOException {
        HashMap<String, Integer> jumps_to_complete = new HashMap<String, Integer>();
        HashMap<String, Integer> label_data = new HashMap<String, Integer>();

        for (ThreeStType statement : code.All_Satement) {
            if (statement.category == Category.binaryGen) {
                binaryGen(statement);

            } else if (statement.category == Category.storageGen) {
                storageGen(statement);
            } else if (statement.category == Category.unaryGen) {
                unaryGen(statement);
            } else if (statement.category == Category.ifGen) {
                emitComment(String.valueOf(statement.type));
                switch (statement.type) {
                    case Str_if: {
                        int temp = Integer.valueOf(statement.temp.substring(1));
                        int left_child = Integer.valueOf(statement.left_child.substring(1));
                        emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "save val of temo into r1");
                        emit_RM_instr(linenum + 1, "ST", 1, (-(temp)), 6, "save val of temp into r1");
                        linenum += 2;
                        break;
                    }

                    case If_start: {
                        int left_child = Integer.valueOf(statement.left_child.substring(1));
                        emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "save val of temp into r1");
                        emitComment((linenum + 1) + ": JEQ 1,?(7)");
                        linenum++;
                        jumps_to_complete.put(statement.right_child, linenum);
                        linenum++;
                    }
                        break;
                    case Label: {
                        label_data.put(statement.left_child, linenum);
                        break;
                    }
                    case Goto: {
                        emitComment(linenum + ": LDA 7,?(7)");
                        jumps_to_complete.put(statement.left_child, linenum);
                        linenum++;
                        break;
                    }
                    case if_end: {

                        int line_Num = label_data.get("L1") - jumps_to_complete.get("L1") - 1;

                        emit_RM_instr(jumps_to_complete.get("L1"), "JEQ", 1, line_Num, 7, "Jumps completed");

                        int line_num = label_data.get("L2") - jumps_to_complete.get("L2");

                        emit_RM_instr(jumps_to_complete.get("L2"), "LDA", 7, line_num, 7, "Goto");

                    }
                }

            }

            else if(statement.category == Category.functionCallGen){
                emitCommentBlock("Function calls not yet implemented fully");
            }
        }
    }

    public void binaryGen(ThreeStType statement) {
        emitComment(String.valueOf(statement.type));

        int left_child = Integer.valueOf(statement.left_child.substring(1));
        int right_child = Integer.valueOf(statement.right_child.substring(1));
        int temp = Integer.valueOf(statement.temp.substring(1));

        switch (statement.type) {
            case Plus: {
            
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "store temp to r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "store temp tp r2");
                emit_RO_instr(linenum + 2, "ADD", 1, 1, 2, "ADD r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "ST", 1, (-(temp)), 6, "Store temp to r1");

                linenum += 4;
                break;
            }

            case Minus: {
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "store temp to r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "store temp tp r2");
                emit_RO_instr(linenum + 2, "SUB", 1, 1, 2, "SUBTRACT r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "ST", 1, (-(temp)), 6, "Store temp to r1");

                linenum += 4;
                break;
            }

            case Times: {
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "store temp to r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "store temp tp r2");
                emit_RO_instr(linenum + 2, "MUL", 1, 1, 2, "MULTIPLY r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "ST", 1, (-(temp)), 6, "Store temp to r1");

                linenum += 4;
                break;
            }

            case Divide: {

                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "store temp to r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "store temp tp r2");
                emit_RO_instr(linenum + 2, "DIV", 1, 1, 2, "DIV r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "ST", 1, (-(temp)), 6, "Store temp to r1");

                linenum += 4;
                break;
            }

            case LessThan: {
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "save val of temo into r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "save val of temo into r2");
                emit_RO_instr(linenum + 2, "SUB", 1, 1, 2, "ADD r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "JLT", 1, linenum + 6, 0, "if true then jump");
                emit_RM_instr(linenum + 4, "LDC", 1, 0, 0, "0 = false");
                emit_RM_instr(linenum + 5, "LDC", 7, linenum + 7, 0, "jump ahead true instruction");
                emit_RM_instr(linenum + 6, "LDC", 1, 1, 0, "1 = true");
                emit_RM_instr(linenum + 7, "ST", 1, (-(temp)), 6, "save value of r1");

                linenum += 8;
                break;
            }
            case Equals: {
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "save val of temo into r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "save val of temo into r2");
                emit_RO_instr(linenum + 2, "SUB", 1, 1, 2, "SUB r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "JEQ", 1, linenum + 6, 0, "if true then jump");
                emit_RM_instr(linenum + 4, "LDC", 1, 0, 0, "0 = false");
                emit_RM_instr(linenum + 5, "LDC", 7, linenum + 7, 0, "jump ahead true instruction");
                emit_RM_instr(linenum + 6, "LDC", 1, 1, 0, "1 = true");
                emit_RM_instr(linenum + 7, "ST", 1, (-(temp)), 6, "save value of r1");
                linenum += 8;
                break;
            }

            case Or: {
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "save val of temo into r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "save val of temo into r2");
                emit_RM_instr(linenum + 2, "JNE", 1, linenum + 6, 0, "JUMP if left true");
                emit_RM_instr(linenum + 3, "JNE", 2, linenum + 6, 0, "JUMP if right true");
                emit_RM_instr(linenum + 4, "LDC", 1, 0, 0, "0 = false");
                emit_RM_instr(linenum + 5, "LDC", 7, linenum + 7, 0, "jump ahead true instruction");
                emit_RM_instr(linenum + 6, "LDC", 1, 1, 0, "1 = true");
                emit_RM_instr(linenum + 7, "ST", 1, (-(temp)), 6, "save value of r1");
                linenum += 8;
                break;

            }

            case And: {
                emit_RM_instr(linenum, "LD", 1, (-(left_child)), 6, "save val of temo into r1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "save val of temo into r2");
                emit_RM_instr(linenum + 2, "JEQ", 1, linenum + 6, 0, "JUMP if left true");
                emit_RM_instr(linenum + 3, "JEQ", 2, linenum + 6, 0, "JUMP if right true");
                emit_RM_instr(linenum + 4, "LDC", 1, 1, 0, "0 = false");
                emit_RM_instr(linenum + 5, "LDC", 7, linenum + 7, 0, "jump ahead true instruction");
                emit_RM_instr(linenum + 6, "LDC", 1, 0, 0, "1 = true");
                emit_RM_instr(linenum + 7, "ST", 1, (-(temp)), 6, "save value of r1");
                linenum += 8;
                break;

            }

        }
    }

    public void unaryGen(ThreeStType statement) {
        emitComment(String.valueOf(statement.type));

        int temp = Integer.valueOf(statement.temp.substring(1));
        int right_child = Integer.valueOf(statement.right_child);

        switch (statement.type) {

            case Negate: {
                emit_RM_instr(linenum, "LD", 1, -1, 0, "r1 = -1");
                emit_RM_instr(linenum + 1, "LD", 2, (-(right_child)), 6, "store temp to r2");
                emit_RO_instr(linenum + 2, "MUL", 1, 1, 2, "MULTIPLY r1 and r2 and store into r1");
                emit_RM_instr(linenum + 3, "ST", 1, (-(temp)), 6, "Store temp to r1");
                linenum += 4;
                break;

            }

            case Not: {
                emit_RM_instr(linenum, "LD", 1, (-(right_child)), 6, "save val of temo into r2");
                emit_RM_instr(linenum + 1, "JEQ", 1, linenum + 4, 0, "JUMP if right true");
                emit_RM_instr(linenum + 2, "LDC", 1, 0, 0, "0 = false");
                emit_RM_instr(linenum + 2, "LDC", 7, linenum + 5, 0, "jump ahead true instruction");
                emit_RM_instr(linenum + 4, "LDC", 1, 0, 0, "1 = false");
                emit_RM_instr(linenum + 5, "ST", 1, (-(temp)), 6, "save value of r1");
                linenum += 6;
                break;

            }

        }
    }

    public void storageGen(ThreeStType statement) {
        emitComment(String.valueOf(statement.type));

        int temp = Integer.valueOf(statement.temp.substring(1));
        int left_child = Integer.valueOf(statement.left_child);
        emit_RM_instr(linenum, "LDC", 1, left_child, 0, "save value into r1");
        emit_RM_instr(linenum + 1, "ST", 1, (-(temp)), 6, "save temp into r1");

        linenum += 2;

    }

    /*
     * public void generate_print(){
     * emit_RM_instr(" "+lineNum(0)+": ST, 1,1,(5) # save R! to stack frame");
     * emit_RM_instr(" "+lineNum(1)+": LD 1,-1,(5) # Load arguement into R1");
     * emit_RO_inst(" "+lineNum(2)+": OUT 1,0,0 # print value of arguement");
     * emit_RM_instr(" "+lineNum(3)+ ": LD 1,1,(5) # restore R1 from stack frame");
     * emit_RM_instr(" "+lineNum(4)+": LD 7,0,(5)");
     * linenum+=5;
     * }
     */



    public void saveRegisters(int low, int high) {
        emitComment("save registers 1,4");
        for (int i = low; i < high + 1; i++) {
            emit_RM_instr(linenum, "ST", i, i, 5, "calling seq: store registers");
            linenum += 1;
        }
    }

    public void restoreRegisters(int low, int high) {
        emitComment("restore registers 1,4");
        for (int i = low; i < high + 1; i++) {
            emit_RM_instr(linenum, "LD", i, i, 5, "return seq: restore registers");
            linenum += 1;
        }
    }

    private void symbol_table(Definition_List ast) {

        ArrayList<ThreeAddrSt> three_codes = new ArrayList<ThreeAddrSt>();

        // check if main is defined
        boolean isMainPresent = false;

        // Loop through the function definitions in the AST
        Iterator<DefinitionNode> it = ast.value().iterator();
        while (it.hasNext()) {
            DefinitionNode def = it.next();
            // Get the function name
            String funcName = def.id().getid();

            if (funcName.equals("main")) {
                isMainPresent = true;
            }

            // Get the number of parameters
            int numParams = def.pl().value().size();

            // Add the FunctionInfo object to the HashMap
            func_symbol_args.put(funcName, numParams);

            // FunctionInfo_Temp temp = new FunctionInfo_Temp(funcName, 0);
            func_symbol_args_temp.put(funcName, 0);

            // function arguments lookup
            function_arg_lookup.put(def.id().getid(), new HashMap<String, Integer>());

            for (int i = 0; i < def.pl().value().size(); i++) {
                function_arg_lookup.get(def.id().getid()).put(def.pl().get(i).id().getid(), i + 1);
            }

            func_three_code.put(funcName, three_codes);
            // Loop through each expression in the functions body
            for (Expression exp : def.body().exp()) {
                ThreeAddrSt stmt = new ThreeAddrSt(exp);
                // add the statement to the list of code
                three_codes.add(stmt);

                // Update the number of temps used by the temps
                int three_adr_temp = ThreeAddrSt.temp - 1;
                if (func_symbol_args_temp.get(funcName) < three_adr_temp) {
                    func_symbol_args_temp.put(funcName, three_adr_temp);
                }
                // reset the temp counter for the next expression
                ThreeAddrSt.temp = 1;
            }
        }

        if (isMainPresent == false) {
            System.out.println("The main function does not exist");
            System.exit(-1);
        }

    }


    public void emit_RO_instr(int line_num, String opcode, int r1, int r2, int r3, String comment) {
        String registerStr = String.format("%d,%d,%d", r1, r2, r3);
        String formatStr = "%4d: %4s %10s # %s";
        file_writer(String.format(formatStr, line_num, opcode, registerStr, comment));

    }

    public void emit_RM_instr(int line_num, String opcode, int r1, int offset, int r2, String comment) {
        String registerStr = String.format("%d,%d(%d)", r1, offset, r2);
        String formatStr = "%4d: %4s %10s # %s";
        file_writer(String.format(formatStr, line_num, opcode, registerStr, comment));
        
    }

    public void emitComment(String comment) {
        file_writer(String.format("* %s", comment));
    }

    public void emitCommentBlock(String headStr) {
        String str = "-";
        for (int i = 0; i<6; i++){
            str = str+str;
        }
        //emitComment(str.repeat(50));
        emitComment(str);
        emitComment("--- " + headStr);
    }

    private void file_writer(String format_str) {
        try {
            FWriter.write(format_str + "\n");
            FWriter.flush();
        } catch (IOException e) {
            System.out.println("unable to write '" + format_str + "' to file");
        }
    }

}
