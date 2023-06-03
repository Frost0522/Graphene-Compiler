////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////                                                                                                      /////
/////                                           Graphene Compiler                                          /////
/////                                                                                                      /////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Team Name:

	* JT

Contributors:

	* Jacob Frost, Tanzeel Ur Rehman

Running the project:

1- Make sure the file you want to compile is in the same directory as the unix script
2- To run the Scanner on a given file: ./graphenes filename.gr
3- To run the parser and check if the program is legal on a given file: ./graphenef filename.gr
4- To construct the AST and print it out on a given file: ./graphenep filename.gr
5- To Run the CodeGenerator and create a .tm file on a given file: ./graphenec filename.gr

List of files and general layout:
	
	* Classes - Contains all of the classes for the scanner, parser, lexical errors, executable files, and unix command
	  scripts. 

	* Documents - In this directory there will be instructions for compiling source code, the tokens used in the 
	  language along with grammer for the Graphene language, the language without grammer ambiguity, the first and 
	  follows sets for the parser, and the parsing table. 

	* Source - Graphenes creates a reader and takes in the file given to it, which is then saved to a string and given 
	  to a scanner object. The Scanner will then traverse the string and produce a dictionary of valid tokens in the
	  language. The Token object has type, value, and corresponding getter methods.   

	* Tests - The scanner object's tests range from 1 to 5 and checks for an invalid variable, checks for comments,    
	  produces an error for an invalid number, produces an error for a number that is out of range, and
	  tests that empty files are valid programs, respectively. The parser object's tests also range from 1 to 5
	  and tests the urnary operators "-", "+", "*", and "<", check that parameter lists will recoginze types
	  integer and boolean, definition bodies recognize valid expressions, comments do not break if and else
	  clauses, and order of if and else expressions, respectively. 
 
	* Programs - The scanner program creates Pascal's triangle, and the parse program will return whether a number N is
	  automorphic or not.

	* Miscellaneous - This directory represents files and parts of this project that do not fall into any of the
	  catagories listed above, there are currently no miscellaneous files.

List of known bugs:

Language features not yet implemented:
	* Type Checker
	* code generator(function calls)

Optimizations:

Design decisions:

	* For a more detailed description see the documents directory for design details and diagrams.


