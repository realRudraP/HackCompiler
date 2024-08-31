package edu.litmus.compiler;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser(args[0]);
        CodeWriter codeWriter = new CodeWriter(args[0]);
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
                    break;
            }
            parser.advance();
        } while (parser.hasMoreCommands());
        parser.close();
        codeWriter.close();
    }
}

