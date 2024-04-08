import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
// Contributors
// Baris Giray Akman 150121822
// Musa Ozkan 150121058
// Abdullah Kan 150121076

// This code implements both lexical analyzer and parse tree and prints the results on both console and on output.txt file.


class main{
    public static int current_line = 1;
    public static int current_index = 0;
    public static int error_line = 0;
    public static int error_position = 0;
    public static FileWriter output;
    public static ArrayList<String> tokens = new ArrayList<>();
    public static ArrayList<String> positions = new ArrayList<>();
    public static ArrayList<String> token_content = new ArrayList<>();
    public static int token_index = 0;
    public static int parser_index = 0;
    public static String nextToken = "";
    public static int indentation_count = 0;


    public static boolean isBracketExist(char token){
    
        return token==')' || token=='(' || token=='{' || token =='{' || token == '[' || token ==']';
    }

    public static boolean printBracket(char token) throws IOException{
        
        if(token == '('){
            output.write("LEFTPAR "+current_line+":"+(current_index+1)+"\n");
            System.out.println("LEFTPAR "+current_line+":"+(current_index+1));
            positions.add(current_line+":"+(current_index+1));
            token_content.add("(");
            tokens.add("LEFTPAR");
            return true;
        }
        
        
        else if(token==')'){
            output.write("RIGHTPAR "+current_line+":"+(current_index+1)+"\n");
            System.out.println("RIGHTPAR "+current_line+":"+(current_index+1));
            tokens.add("RIGHTPAR");
            positions.add(current_line+":"+(current_index+1));
            token_content.add(")");
            return true;
        }   
              

        else if(token=='['){
            output.write("LEFTSQUAREB "+current_line+":"+(current_index+1)+"\n");
            System.out.println("LEFTSQUAREB "+current_line+":"+(current_index+1));
            tokens.add("LEFTSQUAREB");
            positions.add(current_line+":"+(current_index+1));
            token_content.add("[");
            return true;
        }
            
          
        else if(token==']'){
            output.write("RIGHTSQUAREB "+current_line+":"+(current_index+1)+"\n");
            System.out.println("RIGHTSQUAREB "+current_line+":"+(current_index+1));
            tokens.add("RIGHTSQUAREB");
            positions.add(current_line+":"+(current_index+1));
            token_content.add("]");
            return true;
        }
            

        else if(token=='{'){
            output.write("LEFTCURLYB "+current_line+":"+(current_index+1)+"\n");
            System.out.println("LEFTCURLYB "+current_line+":"+(current_index+1));
            tokens.add("LEFTCURLYB");
            positions.add(current_line+":"+(current_index+1));
            token_content.add("{");
            return true;
        }
        
            
        else if(token=='}'){
            output.write("RIGHTCURLYB "+current_line+":"+(current_index+1)+"\n");
            System.out.println("RIGHTCURLYB "+current_line+":"+(current_index+1));
            tokens.add("RIGHTCURLYB");
            positions.add(current_line+":"+(current_index+1));
            token_content.add("}");
            return true;
        }
            
  
        return false;
    }


    // It provides the first condition of hexademical or binary notation.
    // It checks the first two elements of the token.
    public static boolean isHexorBin(String line){

        return (line.length()-current_index-1>=1 && line.charAt(current_index)=='0' && (line.charAt(current_index+1) =='x' || line.charAt(current_index+1)=='b'));

    }



    // Method for boolean literals
    public static boolean booleanLiterals(String line) throws IOException{
        if(current_index>=line.length())
            return true;
        int i;
        String boolean_checker = "";
        for(i=current_index;i<line.length() && line.charAt(i)!= ' ';i++){
            if(isBracketExist(line.charAt(i)))
                break;
            boolean_checker+=line.charAt(i);
        }  
        
        if(boolean_checker.equals("true") || boolean_checker.equals("false")){
            System.out.println("BOOLEAN "+current_line+":"+(current_index+1));
            output.write("BOOLEAN "+current_line+":"+(current_index+1)+"\n");
            current_index = i;
            tokens.add("BOOLEAN");
            positions.add(current_line+":"+(current_index+1));
            token_content.add(boolean_checker);
            if(current_index<line.length() && isBracketExist(line.charAt(current_index)))
                printBracket(line.charAt(i));
            return true;
        }

        return false;

    }

    
    

    public static boolean charLiterals(String line) throws IOException{
        if(current_index>=line.length())
            return true;
        int i = 0;
        if((int)line.charAt(current_index) ==39){
            String error  = "";
            
            


            if(current_index+2==line.length() || (current_index+2<line.length() && (int)line.charAt(current_index+2)!=39)){
                error = line.charAt(current_index)+""+line.charAt(current_index+1);
                output.write("LEXICAL ERROR "+"["+current_line+":"+(current_index+1)+"]:"+" Invalid token "+ "'"+error+"'");
                System.out.println("LEXICAL ERROR "+"["+current_line+":"+(current_index+1)+"]:"+" Invalid token "+ "'"+error+"'");
                output.flush();
                output.close();
                System.exit(0);
            }
            else if(current_index+2<line.length() && (int)line.charAt(current_index+2)==39){
                System.out.println("CHAR "+current_line+":"+(current_index+1));
                output.write("CHAR "+current_line+":"+(current_index+1)+"\n");
                positions.add(current_line+":"+(current_index+1));
                token_content.add("'"+line.charAt(current_index+1)+"'"); // Reminder
                if(line.charAt(current_index+2)=='\'' && line.charAt(current_index+1) == '\\'){
                    current_index+=3;

                }
                else current_index+=2;

                tokens.add("CHAR");
                return true;
            }
                
            
            else if(current_index+2<line.length() && (int)line.charAt(current_index+2)!=39){
                error = line.charAt(current_index)+""+line.charAt(current_index+1);
                output.write("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                System.out.println("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                output.flush();
                output.close();
                System.exit(0);
            }

        }
        return false;

    }

    public static boolean identifierElementCheck(char token){

        return token =='+' || token=='-' || token=='.' || (token>='0' && token<='9') || (token>='a' && token<='z') || (token>='A' && token<='Z');

        
    }
    public static boolean checkIdentifierFirstElement(char token){
        return token=='!' || token=='?' || token=='>' || token=='<' || token=='=' || token=='/' || token==':' || token=='+' || token=='-'|| token=='.' || token=='*' ||
        (token>='a' && token<='z') || (token>='A' && token<='Z');
    }
    public static boolean identifierLiteral(String line) throws IOException{
        if(current_index>=line.length())
            return true;
        int i = current_index;

        String error ="";
        boolean error_exist = false;  
        if(checkIdentifierFirstElement(line.charAt(current_index))){
            error+=line.charAt(current_index);
            for(i = current_index+1;i<line.length() && line.charAt(i) !=' ';i++){
                
                if(isBracketExist(line.charAt(i)))
                    break;

                if(!identifierElementCheck(line.charAt(i))){
                    error_exist = true;
                    error_line = current_line;
                    error_position = current_index+1;
                }
                    
                    
                
                error+=line.charAt(i);

            }

            if(error_exist){
                System.out.println("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                output.write("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                output.flush();
                output.close();
                System.exit(0);
            }
            else{
                System.out.println("IDENTIFIER "+current_line+":"+(current_index+1));
                tokens.add("IDENTIFIER");
                positions.add(current_line+":"+(current_index+1));
                token_content.add(error);
                output.write("IDENTIFIER "+current_line+":"+(current_index+1)+"\n");

                current_index = i;
                if(current_index<line.length() && isBracketExist(line.charAt(current_index)))
                    printBracket(line.charAt(i));
                return true;
            }
            

        }


        return false;
    }


    public static boolean keywordLiteral(String line) throws IOException{

        if(current_index>=line.length())
            return true;
        String identifier = "";
        int i;
        for(i = current_index;i<line.length() && line.charAt(i) != ' ';i++){
            
            if(isBracketExist(line.charAt(i)))
                break;
            identifier+=line.charAt(i);
            
        }

        if(identifier.equals("define") || identifier.equals("let") || identifier.equals("cond") || identifier.equals("if") ||identifier.equals("begin")){
            System.out.println(identifier.toUpperCase()+" "+current_line+":"+(current_index+1));
            output.write(identifier.toUpperCase()+" "+current_line+":"+(current_index+1)+"\n");
            current_index = i;
            tokens.add(identifier);
            positions.add(current_line+":"+(current_index+1));
            token_content.add(identifier);
            if(current_index<line.length() && isBracketExist(line.charAt(current_index)))
                printBracket(line.charAt(i));
            return true;
            
        }
        return false;
            
    
    }



    // Function for number literals.
    public static boolean numberLiterals(String line) throws IOException{

        if(current_index>=line.length())
            return true;
        // Hexadecimal condition 
        // This condition checks the input according to hexadecimal notation and binary notation.
        int i = 0;
        String error = "";
        boolean error_exist = false;

        // It includes hexadecimal literal and binary literal in
        if(isHexorBin(line)){
            error = "0";
            
            if(line.length()-current_index-1==1){
                error_position = current_index+1;
                error_line = current_line;
                error_exist = true;
                error += line.charAt(current_index)+line.charAt(current_index+1);
            }
            else{
                
                // Before iteration, index of the first element of token and the line it's located is conserved.
                error_position = current_index+1;
                error_line = current_line;
                boolean isHex = false;
                if(line.charAt(current_index+1)=='x'){
                    isHex = true;
                    error+="x";
                }
                else{
                    isHex = false;
                    error+="b";
                }

                // Checks along the token
                for(i = current_index+2;i<line.length() && line.charAt(i)!=' ';i++){
                    
                    if(isBracketExist(line.charAt(i)))
                        break;
                    // Adjusting the error variable even if there is no error exists.
                    error+=line.charAt(i);
                    

                    // If input isn't convenient with hexadecimal literal, error happens.
                    if(isHex && !((line.charAt(i)>='A' && line.charAt(i)<='F') || (line.charAt(i)>='0' && line.charAt(i)<='9') || (line.charAt(i)>='a' && line.charAt(i)<='f')))
                        error_exist = true;
                       

                    // If input isn't convenient with binary literal, error happens.
                    else if( !isHex && line.charAt(i)!='0' && line.charAt(i)!='1'){
                        error_exist = true;
                    }
                }
            }
            // If there is error, it is displayed on the console and program is shut down
            if(error_exist){
                output.write("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                System.out.println("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                output.flush();
                output.close();
                System.exit(0);
            }

            // Otherwise, line and position that token is been is displayed on the console.
            else{
                output.write("NUMBER "+current_line+":"+(current_index+1)+"\n");
                System.out.println("NUMBER "+current_line+":"+(current_index+1));
                current_index = i;
                tokens.add("NUMBER");
                positions.add(current_line+":"+(current_index+1));
                token_content.add(error);
                if(current_index<line.length() && isBracketExist(line.charAt(current_index)))
                    printBracket(line.charAt(i));
                return true;
            }          
        }


        // Write a distinct case for a situation where first element of token is E or e !
        // It includes number literal and floating point literal in one.
        else if(line.charAt(current_index) == '.' || line.charAt(current_index)=='+' || line.charAt(current_index)=='-' || (line.charAt(current_index)>='0' && line.charAt(current_index)<='9')){

           
            error+=line.charAt(current_index);
            error_position = current_index+1;
            error_line = current_line;

            boolean isFloatFirst = false; // Boolean expression that express if statement is convenient with first floating point literal 
            boolean exponential_used = false; // Boolean expression that express if E or e exist in the token or not.

            if(line.charAt(current_index)=='.')
                isFloatFirst = true;
            for(i = current_index+1; i<line.length() && line.charAt(i)!=' ';i++){
                

                if(isBracketExist(line.charAt(i))){
                    break;
                }

                // If the element of the token is not convenient with floating point literal, error occurs.
                if(line.charAt(i) != 'E' && line.charAt(i) !='e' && line.charAt(i)!='+' && line.charAt(i) !='-' && line.charAt(i)!='.' && (line.charAt(i)<'0' || line.charAt(i)>'9')){
                    
                    error_exist = true;
                    error_line = current_line;
                    error_position = current_index+1;
                }   
                    
                    

                // If there is more than one exponential symbol in the token, error occurs.
                else if(exponential_used && (line.charAt(i)=='e' || line.charAt(i)=='E' || line.charAt(i)=='.')){
                    error_exist = true;
                    error_line = current_line;
                    error_position = current_index+1;
                }
                    


                // If there is a symbol other than exponential symbol before addition or subtraction, error occurs.
                else if(i>=0 && (line.charAt(i-1)!='e' && line.charAt(i-1)!='E') && (line.charAt(i) =='+' || line.charAt(i)=='-')){
                    error_exist = true;
                    error_line = current_line;
                    error_position = current_index+1;
                }
                    


                // If there is more than one dot sign, error occurs.
                else if(isFloatFirst && line.charAt(i)=='.'){
                    error_exist = true;
                    error_line = current_line;
                    error_position = current_index+1;
                }

                else if(i>=0  && (line.charAt(i-1)<'0' || line.charAt(i-1)>'9') && (line.charAt(i)=='E' || line.charAt(i)=='e')){
                    error_exist = true;
                    error_line = current_line;
                    error_position = current_index+1;
                }
                    

                // It arranges boolean expression if element is exponential.
                if(line.charAt(i) =='E' || line.charAt(i)=='e')
                    exponential_used = true;


                // Even if there is no error, token's elements are held as a string representation.
                error += line.charAt(i);

                // If dot sign exist in the token, program will iterate according to floating point literal.
                if(line.charAt(i)=='.')
                    isFloatFirst = true;

            }

            
            int last_element = i-1;
            if(line.charAt(last_element) =='+' || line.charAt(last_element)=='-' || line.charAt(last_element)=='E' || line.charAt(last_element)=='.' || line.charAt(last_element)=='e')
                error_exist = true;

            // If there is error, it is displayed on the console and program is shut down
            if(error_exist){
                output.write("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                System.out.println("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
                output.flush();
                output.close();
                System.exit(0);
            }

            // Otherwise, line and position that token is been is displayed on the console.
            else{
                System.out.println("NUMBER "+current_line+":"+(current_index+1));
                output.write("NUMBER "+current_line+":"+(current_index+1)+"\n");
                tokens.add("NUMBER");
                positions.add(current_line+":"+(current_index+1));
                token_content.add(error);
                current_index = i;

                if(current_index<line.length() && isBracketExist(line.charAt(current_index)))
                    printBracket(line.charAt(current_index));
                return true;
            }

        }

        return false;
        
        // It checks the conditions according to decimal integer and floating point notation.

        
    }

    public static void printArray(){
        for(int i = 0;i<tokens.size();i++)
            System.out.println(tokens.get(i));

    }

    public static void main(String [] args) throws IOException{
        

        try{
            output = new FileWriter("output.txt");
            String error_for_string = "\"";
            Scanner file_input = new Scanner(System.in);
            System.out.print("Please enter the name of your file: ");
            String file_name = file_input.nextLine();
            File file = new File(file_name);
            Scanner input = new Scanner(file);
            boolean string_literal = false;
            int string_literal_line = 0;
            int string_literal_index = 0;
            boolean isStringComplete = true;
            // Iteration through lines
            while(input.hasNextLine()){

                String line = input.nextLine();
                
                // It iterates through the line.

                
                while(current_index<line.length()){
                    
                    
                    if(string_literal && current_index+1<line.length() && line.charAt(current_index)=='\\' && line.charAt(current_index+1) =='\"'){
                        error_for_string+=line.charAt(current_index);
                        current_index+=2;
                        if(current_index>line.length()-1)
                            break;
                    }
                    else if(string_literal && line.charAt(current_index)=='"'){
                        string_literal = false;
                        System.out.println("STRING "+string_literal_line+":"+(string_literal_index+1));
                        output.write("STRING "+string_literal_line+":"+(string_literal_index+1)+"\n");
                        tokens.add("STRING");
                        positions.add(string_literal_line+":"+(string_literal_index+1));
                        token_content.add(error_for_string+"\"");

                        error_for_string ="";
                        current_index++;
                        
                        if(current_index>line.length()-1)
                            break;
                        
                    }
                    else if(string_literal)
                        error_for_string+=line.charAt(current_index);
                    

                    if(line.charAt(current_index)=='~'){
                        System.out.println("COMMENT "+current_line+":"+(current_index+1));
                        output.write("COMMENT "+current_line+":"+(current_index+1)+"\n");
                        break;
                    }
                    
                    if(!string_literal && line.charAt(current_index)=='"'){
                        string_literal_line = current_line;
                        string_literal_index = current_index;
                        string_literal = true;
                    }
                    
                    boolean isFound = false;
                    // As long as there is no space, other circumstances are considered.

                    
                    if(!string_literal && line.charAt(current_index)!=' '){


                        if((checkIdentifierFirstElement(line.charAt(current_index)) && current_index==line.length()-1) || ((current_index<line.length()-1 && line.charAt(current_index+1)==' ') &&
                        (line.charAt(current_index)=='+' || line.charAt(current_index)=='-' || line.charAt(current_index)=='.'))){
                            System.out.println("IDENTIFIER "+current_line+":"+(current_index+1));
                            tokens.add("IDENTIFIER");
                            positions.add(current_line+":"+(current_index+1));
                            
                            token_content.add(""+line.charAt(current_index));
                            output.write("IDENTIFIER "+current_line+":"+(current_index+1)+"\n");
                            isFound = true;
                        }
                        
                        else{

                            isFound = numberLiterals(line);
                        } 
                        
                        if(!isFound)
                            isFound = keywordLiteral(line);

                        if(!isFound)
                            isFound = booleanLiterals(line);

                        
                        if(!isFound){
                            isFound = identifierLiteral(line);
                        }

                        if(!isFound)
                            isFound = printBracket(line.charAt(current_index));

                        if(!isFound)
                            isFound = charLiterals(line);

                        // Before number literal, identifier literal will be settled here.
                        // Other literals will be here!
                    }
                    
                    current_index++;
                }
                current_line++;
                current_index = 0;
            }
            if(string_literal){
                
                System.out.println("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error_for_string+"'");
                output.write("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error_for_string+"'");
                output.flush();
                output.close();
                System.exit(0);
            }
            input.close();

            nextToken = tokens.get(parser_index);
            Parser parser = new Parser(tokens, token_content, positions, output);
            parser.program();

            output.close();
                
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error occured.");
            
        }
        
    }
}