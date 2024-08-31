package edu.litmus.compiler;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        Parser parser=null;
        CodeWriter codeWriter=null;
        try{
            if(args.length==0){
                throw new IllegalArgumentException("No input file provided");
            }
        parser = new Parser(args[0]);
        codeWriter = new CodeWriter(args[0]);
        do {
            switch (parser.commandType()) {
                case C_ARITHEMTIC:
                    codeWriter.writeArithmetic(parser.arg0());
                    break;
                case C_POP:
                    codeWriter.writePop(parser.arg1(), Integer.parseInt(parser.arg2()));
                    break;
                case C_PUSH:
                    codeWriter.writePush(parser.arg1(), Integer.parseInt(parser.arg2()));
                    break;
                default:
                    throw new SyntaxErrorException("Unknown Command type");
            }
            parser.advance();
        } while (parser.hasMoreCommands());
        parser.close();
        codeWriter.close();
    }catch(IOException e){
        System.out.println("IOException encountered: "+e.getMessage());
    }catch(IllegalArgumentException e){
        System.out.println("Argument Error: "+e.getMessage());
    }catch(SyntaxErrorException e){
        System.out.println("Syntax Error: "+e.getMessage());
    }catch(Exception e){
        System.out.println("Unknown Error: "+e.getMessage());
    }
}
}

