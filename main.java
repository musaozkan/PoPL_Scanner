import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;

import java.io.File;
import java.io.FileNotFoundException;


class main{
    public static int current_line = 1;
    public static int current_index = 0;
    public static int error_line = 0;
    public static int error_position = 0;


    // Conditions for brackets

    // Haven't finished yet. I will check later on.
    public static void checkBrackets(char token){
        
        if(token == '(')
            System.out.println("LEFTPAR "+current_line+":"+(current_index+1));
            
            
        
        else if(token==')')
            System.out.println("RIGHTPAR "+current_line+":"+(current_index+1));
           
            

        else if(token=='[')
            System.out.println("LEFTSQUAREB "+current_line+":"+(current_index+1));
          
            

        else if(token==']')
            System.out.println("RIGHTSQUAREB "+current_line+":"+(current_index+1));
           
            

        else if(token=='{')
            System.out.println("LEFTCURLYB "+current_line+":"+(current_index+1));
            
        else if(token=='}')
            System.out.println("RIGHTCURLYB "+current_line+":"+(current_index+1));
  
   
    }


    // It provides the first condition of hexademical or binary notation.
    // It checks the first two elements of the token.
    public static boolean isHexorBin(String line){

        return (line.length()-current_index-1>=1 && line.charAt(current_index)=='0' && (line.charAt(current_index+1) =='x' || line.charAt(current_index+1)=='b'));

    }



    // Method for boolean literals
    public static void booleanLiterals(String line){

        int i;

        for(i=current_index;i<line.length();i++){
            
        }

    }

    
    // Haven't brainstormed yet = )
    public static void stringLiterals(String line){


    }

    public void charLiterals(String line){

        if((int)line.charAt(current_index) ==39){

            if(current_index+2<=line.length()-1 && (int)line.charAt(current_index+2) !=39 && (int)line.charAt(current_index+2) !=39 && line.charAt(current_index+1)!='\''){
                System.out.println("ERROR!");
                System.exit(0);
            }
        }


    }

    // Function for number literals.
    public static void numberLiterals(String line){


        // Hexadecimal condition 
        // This condition checks the input according to hexadecimal notation and binary notation.
        int i = current_index+2;
        String error = "0";
        boolean error_exist = false;

        // It includes hexadecimal literal and binary literal in one.
        if(isHexorBin(line)){
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
                for(;i<line.length() && line.charAt(i)!=' ';i++){
                    
                    // Adjusting the error variable even if there is no error exists.
                    error+=line.charAt(i);
                    

                    // If input isn't convenient with hexadecimal literal, error happens.
                    if(isHex && !((line.charAt(i)>='A' && line.charAt(i)<='F') || (line.charAt(i)>='0' && line.charAt(i)<='9')))
                        error_exist = true;
                       

                    // If input isn't convenient with binary literal, error happens.
                    else if( !isHex && line.charAt(i)!='0' && line.charAt(i)!='1'){
                        error_exist = true;
                    }
                        
                    
                }
            }
        }

        // It includes number literal and floating point literal in one.
        else if(line.charAt(current_index)=='+' || line.charAt(current_index)=='-' || (line.charAt(current_index)>='0' && line.charAt(current_index)<='9')){

            error_position = current_index+1;
            error_line = current_line;

            current_index++;
            boolean isFloatFirst = false; // Boolean expression that express if statement is convenient with first floating point literal 
            boolean exponential_used = false; // Boolean expression that express if E or e exist in the token or not.

            for(i = current_index+1; i<line.length() && line.charAt(i)!=' ';i++){
                

                
                // If the element of the token is not convenient with floating point literal, error occurs.
                if(line.charAt(i) != 'E' && line.charAt(i) !='e' && line.charAt(i)!='+' && line.charAt(i) !='-' && line.charAt(i)<='0' && line.charAt(i)>='9')
                    error_exist = true;
                    

                // If there is more than one exponential symbol in the token, error occurs.
                else if(exponential_used && (line.charAt(i)=='e' || line.charAt(i)=='E'))
                    error_exist = true;


                // If there is a symbol other than exponential symbol before addition or subtraction, error occurs.
                else if(i>=0 && (line.charAt(i-1)!='e' && line.charAt(i-1)!='E') && (line.charAt(i) =='+' || line.charAt(i)=='-'))
                    error_exist = true;


                // If there is more than one dot sign, error occurs.
                else if(isFloatFirst && line.charAt(i)=='.')
                    error_exist = true;

                // It arranges boolean expression if element is exponential.
                if(line.charAt(i) =='E' || line.charAt(i)=='e')
                    exponential_used = true;


                // Even if there is no error, token's elements are held as a string representation.
                error += line.charAt(i);

                // If dot sign exist in the token, program will iterate according to floating point literal.
                if(line.charAt(i)=='.')
                    isFloatFirst = true;
                


            }


            int last_element = line.length()-1;
            if(line.charAt(last_element) =='+' || line.charAt(last_element)=='-' || line.charAt(last_element)=='E' || line.charAt(last_element)=='.' || line.charAt(last_element)=='e')
                error_exist = true;

        }


        // If there is error, it is displayed on the console and program is shut down
        if(error_exist){
            System.out.println("LEXICAL ERROR "+"["+error_line+":"+error_position+"]:"+" Invalid token "+ "'"+error+"'");
            System.exit(0);
        }

        // Otherwise, line and position that token is been is displayed on the console.
        else{
            System.out.println("NUMBER"+current_line+":"+(current_index+1));
            current_index = i;
        }   
            
            

        // It checks the conditions according to decimal integer and floating point notation.


        
    }


    public static void main(String [] args){
        

        try{
            File file = new File("input.txt");
            Scanner input = new Scanner(file);
            String string_literal = "";
            boolean isStringComplete = true;
            // Iteration through lines
            while(input.hasNextLine()){

                String line = input.nextLine();
                
                // It iterates through the line.
                while(current_index<line.length()){
                    
                    
                    // As long as there is no space, other circumstances are considered.
                    if(line.charAt(current_index)!=' '){
                        checkBrackets(line.charAt(current_index));


                        // Before number literal, identifier literal will be settled here.
                        numberLiterals(line);
                        

                        // Other literals will come here!
                    }
                    if(!isStringComplete)
                        string_literal+=line.charAt(current_index);
                    current_index++;
                    
                }
                

                current_line++;
                current_index = 0;
            }
            input.close();

                
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
            e.printStackTrace();
        }
        
    }
}