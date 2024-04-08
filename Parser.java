import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public ArrayList<String> tokens;
    public int parser_index;
    public ArrayList<String> token_content;
    public ArrayList<String> positions;
    public FileWriter output;
    public String nextToken;
    public int indentation_count;

    public Parser(ArrayList<String> tokens, ArrayList<String> token_content, ArrayList<String> positions, FileWriter output) {
        this.tokens = tokens;
        this.parser_index = 0; // initialize parser index to 0
        this.token_content = token_content;
        this.positions = positions;
        this.output = output;
        this.nextToken = tokens.get(parser_index);
        this.indentation_count = 0;
    }
    public void getNextToken() throws IOException{
		int size = this.tokens.size();
		int stringLength = this.tokens.get(parser_index).length();
		String s1 = String.format("%"+ (indentation_count+stringLength+token_content.get(parser_index).length()+3) +"s\n",tokens.get(parser_index)+" ("+token_content.get(parser_index)+")");
		System.out.print(s1);
        output.write(s1);
		if(parser_index < size-1)
			parser_index++;
		
		nextToken = this.tokens.get(parser_index);
	}
	
	public void program() throws IOException{

        String s1 = String.format("%"+ (indentation_count+9) +"s\n","<Program>");
		output.write(s1);

		System.out.print(s1);
		indentation_count++;
		if(nextToken.equals("LEFTPAR")) {
			topLevelForm();
			program();
			indentation_count--;
		}
		else {
			System.out.printf("%"+(indentation_count+6) +"s\n","    __");
            output.write(String.format("%"+(indentation_count+6) +"s\n","    __"));
			indentation_count--;
			return;
		}
	}
	
	public void topLevelForm() throws IOException{

        String s1 = String.format("%"+ (indentation_count+14) +"s\n","<ToplevelForm>");
		output.write(s1);
		System.out.print(s1);

		indentation_count++;

		if(nextToken.equals("LEFTPAR")) {
			getNextToken();
			secondLevelForm();

			if(nextToken.equals("RIGHTPAR")) {
				getNextToken();
			}
			else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
            indentation_count--;
				
		}else {
			System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
            output.flush();
            output.close();
			System.exit(0);
		}
	}
	
	public void secondLevelForm() throws IOException{
        String s1 = String.format("%"+ (indentation_count+17) +"s\n","<SecondLevelForm>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;
		
		if(nextToken.equals("define")) {
			definition();
			indentation_count--;
		}
		else if(nextToken.equals("LEFTPAR")) {
            getNextToken();

            if(nextToken.equals("IDENTIFIER"))
			    funCall();
            else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
			indentation_count--;
			
			if(nextToken.equals("RIGHTPAR")) {
				getNextToken();
			}else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')'is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
				
		}
        else{
           
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'define' or '(' expected.");
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'define' or '(' expected.");
            output.flush();
            output.close();
            System.exit(0);
            
        } 
	}
	
	public void definition() throws IOException{
        String s1 = String.format("%"+ (indentation_count+12) +"s\n","<Definition>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;
		
		getNextToken();// token was on define and now points next token
		definitionRight();
		indentation_count--;
	}
	
	
	public void definitionRight() throws IOException {
        String s1 = String.format("%"+ (indentation_count+17) +"s\n","<DefinitionRight>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("IDENTIFIER")) {
			getNextToken();
			expression();
			indentation_count--;
		}
		
		else if(nextToken.equals("LEFTPAR")) {
			getNextToken();
            
            if(nextToken.equals("IDENTIFIER"))
			    getNextToken();
            else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
			argList();
			if(nextToken.equals("RIGHTPAR")) {
				getNextToken();
			}else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
				
			statements();
			indentation_count--;
		}
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected." );
            output.flush();
            output.close();
            System.exit(0);
        } 
	}
	
    public void argList() throws IOException{
        String s1 = String.format("%"+ (indentation_count+9) +"s\n","<ArgList>");
		output.write(s1);
		System.out.print(s1);

		indentation_count++;
		
		if(nextToken.equals("IDENTIFIER")) {
			getNextToken();
			argList();
			indentation_count--;
		}
		else {
			System.out.printf("%"+(indentation_count+6) +"s\n","    __");//burada problem var gibi
			output.write(String.format("%"+(indentation_count+6) +"s\n","    __"));
            indentation_count--;
			return;														// indentation_count burada da indirilebilir				
		}
	}
	
	public void statements() throws IOException{
        String s1 = String.format("%"+ (indentation_count+12) +"s\n","<Statements>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("define")) {
			definition();
			statements();			
			indentation_count--;
		}
		else if(!nextToken.equals("RIGHTPAR")){
			expression();
			indentation_count--;
		}
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' or define is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' or define is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
	}
	
    // Look back at expressions
	public void expressions() throws IOException{
        String s1 = String.format("%"+ (indentation_count+13) +"s\n","<Expressions>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("IDENTIFIER") || nextToken.equals("NUMBER") || nextToken.equals("CHAR") || nextToken.equals("BOOLEAN") || 
		   nextToken.equals("LEFTPAR") || nextToken.equals("STRING")) {
			expression();
			expressions();			
			indentation_count--;
		}
		else {
			System.out.printf("%"+(indentation_count+6) +"s\n","    __");
            output.write(String.format("%"+(indentation_count+6) +"s\n","    __"));		
			indentation_count--;
			return;
		}
	}
	
	public void expression() throws IOException{	//IDENTIFIER | NUMBER | CHAR | BOOLEAN | STRING | ( <Expr> )
        String s1 = String.format("%"+ (indentation_count+12) +"s\n","<Expression>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("IDENTIFIER") || nextToken.equals("NUMBER") || nextToken.equals("CHAR") || nextToken.equals("BOOLEAN") || nextToken.equals("STRING")) {
			getNextToken();
			indentation_count--;
		}
		
		else if(nextToken.equals("LEFTPAR")) {
			getNextToken();
			expr();
			if(nextToken.equals("RIGHTPAR")) {
				getNextToken();
			}else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
            indentation_count--;
			
		}
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier', 'number', char, boolean, 'string', or '(' expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier', 'number', 'char', 'boolean', 'string', or '(' expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
    
        
	}
	
	public void expr() throws IOException{
        String s1 = String.format("%"+ (indentation_count+6) +"s\n","<Expr>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("let")) {
			letExpression();
			indentation_count--;
		}
		
		else if(nextToken.equals("cond")) {
			condExpression();
			indentation_count--;
		}
		
		else if(nextToken.equals("if")) {
			ifExpression();
			indentation_count--;			
		}

        else if(nextToken.equals("begin")) {
			beginExpression();
			indentation_count--;			
		}
		
		else if(nextToken.equals("IDENTIFIER")) {
			funCall();
			indentation_count--;			
		}
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'let', 'cond', 'if', 'begin', or 'identifier' expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'let', 'cond', 'if', 'begin', or 'identifier' expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
	}
	
	public void funCall() throws IOException{
        String s1 = String.format("%"+ (indentation_count+9) +"s\n","<FunCall>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		getNextToken();
		expressions();
		
		indentation_count--;
	}
	
	public void letExpression() throws IOException{
		System.out.printf("%"+ (indentation_count+15) +"s\n","<LetExpression>");
        String s1 = String.format("%"+ (indentation_count+15) +"s\n","<LetExpression>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		getNextToken();
		letExpr();
		indentation_count--;
	}
	
	public void letExpr() throws IOException{
        String s1 = String.format("%"+ (indentation_count+9) +"s\n","<LetExpr>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("LEFTPAR")) {
			getNextToken();
			varDefs();
			if(nextToken.equals("RIGHTPAR"))
				getNextToken();
			else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
			
			statements();
			indentation_count--;
		}
		else if(nextToken.equals("IDENTIFIER")) {		//IDENTIFIER ( <VarDefs> ) <Statements>
			getNextToken();//now token is on OPENRD

            if(nextToken.equals("LEFTPAR"))
			    getNextToken();//token was on OPENRD and now next OPENRD		BURALAR PATLAYABİLİR VAZİYET ALALIM
            else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
			varDefs();
			if(nextToken.equals("RIGHTPAR"))
				getNextToken();
			else{
                System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
				
			statements();
			indentation_count--;
		}
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' or '(' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' or '(' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
        
	}
	
	public void varDefs() throws IOException{
        String s1 = String.format("%"+ (indentation_count+9) +"s\n","<VarDefs>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

        if(nextToken.equals("LEFTPAR"))
		    getNextToken();
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
        if(nextToken.equals("IDENTIFIER"))
		    getNextToken();//token was on OPENRD and now on IDENTIFIER		sıkıntı çıkarsa buradaki getNextToken'lardan birini önceki fonksiyonun else if'inin içine koyup deneyelim
        
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'identifier' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
		expression();
		if(nextToken.equals("RIGHTPAR"))
			getNextToken();
		else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: ')' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
		varDef();
		indentation_count--;
	}
	
	public void varDef() throws IOException{
        String s1 = String.format("%"+ (indentation_count+8) +"s\n","<VarDef>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;
		
		if(nextToken.equals("LEFTPAR")) {
            
			varDefs();	
			indentation_count--;
		}
		else {
			System.out.printf("%"+(indentation_count+6) +"s\n","    __");		
            output.write(String.format("%"+(indentation_count+6) +"s\n","    __"));	
			indentation_count--;
			return;
		}
	}
	
	public void condExpression() throws IOException{
        String s1 = String.format("%"+ (indentation_count+16) +"s\n","<CondExpression>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

        if(nextToken.equals("cond"))
		    getNextToken();
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'cond' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'cond' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
		condBranches();
		indentation_count--;
	}
	
	public void condBranches() throws IOException{
        String s1 = String.format("%"+ (indentation_count+14) +"s\n","<CondBranches>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

        if(nextToken.equals("LEFTPAR"))
		    getNextToken();
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: '('is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: '(' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
		expression();
		statements();
		if(nextToken.equals("RIGHTPAR"))
			getNextToken();
		else{
            System.out.println("SYNTAX ERROR in condBranches ["+positions.get(parser_index)+"]: ')' is expected.");
            output.write("SYNTAX ERROR in condBranches ["+positions.get(parser_index)+"]: ')' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
			
		condBranch();
		indentation_count--;
	}
	
	public void condBranch() throws IOException{
        String s1 = String.format("%"+ (indentation_count+12) +"s\n","<CondBranch>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("LEFTPAR")) {
			getNextToken();
			expression();
			statements();
			indentation_count--;
			if(nextToken.equals("RIGHTPAR"))
				getNextToken();
			else{
                System.out.println("SYNTAX ERROR in condBranch ["+positions.get(parser_index)+"]: ')' is expected.");
                output.write("SYNTAX ERROR in condBranch ["+positions.get(parser_index)+"]: ')' is expected.");
                output.flush();
                output.close();
                System.exit(0);
            }
		}
		else {
			System.out.printf("%"+(indentation_count+6) +"s\n","    __");
			indentation_count--;
			return;
		}
	}
	
	public void ifExpression() throws IOException{
        String s1 = String.format("%"+ (indentation_count+14) +"s\n","<IfExpression>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

        if(nextToken.equals("if"))
		    getNextToken();
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'if' is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: 'if' is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
		expression();
		expression();
		endExpression();
		indentation_count--;

	}
	
	public void endExpression() throws IOException{
        String s1 = String.format("%"+ (indentation_count+15) +"s\n","<EndExpression>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

		if(nextToken.equals("IDENTIFIER") || nextToken.equals("NUMBER") || nextToken.equals("CHAR") || nextToken.equals("BOOLEAN") || nextToken.equals("LEFTPAR") ||
        nextToken.equals("STRING")) {
			expression();
			indentation_count--;
		}
		else {
			indentation_count--;
            output.write(String.format("%"+(indentation_count+6) +"s\n","    __"));
			System.out.printf("%"+(indentation_count+6) +"s\n","    __");			
			return;
		}
	}
	
	public void beginExpression() throws IOException{
        String s1 = String.format("%"+ (indentation_count+17) +"s\n","<BeginExpression>");
		output.write(s1);
		System.out.print(s1);
		indentation_count++;

        if(nextToken.equals("begin"))
		    getNextToken();
        else{
            System.out.println("SYNTAX ERROR ["+positions.get(parser_index)+"]: BEGIN is expected.");
            output.write("SYNTAX ERROR ["+positions.get(parser_index)+"]: BEGIN is expected.");
            output.flush();
            output.close();
            System.exit(0);
        }
		statements();
		indentation_count--;
		
	}



}
