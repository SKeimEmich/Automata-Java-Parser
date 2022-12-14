# Automata-Java-Parser
Parser for basic Java statement (limited to single method parsing with no library or class imports)

Google Doc link for final definitions and contributions (Per assignment requirements)
https://docs.google.com/document/d/1FIzxBgzkDR7rE23oBuvWadgyuqCUfLK05Yu3Hs-aUHw/edit?usp=sharing


The Tigers - 
Jonathan Espinosa
Sam Keim
Katie Tracy

Language:
* Java (not in its entirety) parser, programmed in Java language.
* Our Java program will read in a file containing a method written in Java with no classes (except System.out.println) and parse it.  If there is an invalid statement then the program will cease parsing and output the error and where it was found in the file. 

Definitions:
* Data Type: Valid data type matching a Java reserved keyword, ie. int, double, char
* Variable: Declared variable name
* Value: Hardcoded value, ie. 1, 1.0, 'c'
* u-char -> A | B | C | D | E | F | G | H | I | J | K | L | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z
* char -> a | b | c | d | e | f | g | h | i | j | k | l | m | n | o | p | q | r | s | t | u | v | w | x | y | z
* num -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
* alpha-num -> char | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9

Syntax used in this description:
* Upper Case: Variable
* lower case: Terminal
* \[Square Brackets\]: An optional or repeated block
* Question Mark?: Follows an optional expression x ^ (0 or 1)

Statement Types
* If Statement -> if (Boolean Expression) Statement
* For Statement -> for ([Simple Statement comma deliminated]*; Boolean Expression; [Simple Statement comma deliminated]*) Statement
  * var init: <var name><=><value|operation><;>
  * var increment: <var name><++><;>
  * var decrement: <var name><--><;>
* Variable Name -> char+[alpha-num]*
  * We'll have an array of keywords to check that the variable is not any of them
* Switch Statement -> switch (Variable) {[case DataType: [Statement]*]* [default:]?}
* While Statement -> while (Boolean Expression) Statement
* Do-While Statement -> do Statement while (Boolean Expression);
* Simple Statement -> [Data Type]? Variable = [Value or Variable] [[+, -, *, /] [Value or Variable]]*;
* Compound Statement -> {[Statement, Any]*}
* Method Signature -> [Access Modifier]? [static]? void [Method Name] () { Statement }
* Boolean Expression -> [Variable or Value] [>, <, ==, !=, >=, <=] [Variable or Value] [[&&, ||] [Variable or Value] [>, <, ==, !=, >=, <=] [Variable or Value]]*
* Print-Stmt -> <System.out.print><left paren> <var name | string | operation> <right paren><;> //limit to string literals and variable names?

Stretch Goals/Currently not included officially: 
* Comments -> Lines proceeded by // or encased by /* */
* class -> public class-name {main-method}
* class-name -> u-char(alpha-num)*
* Parameter Passing and Return Types 
* Arrays
