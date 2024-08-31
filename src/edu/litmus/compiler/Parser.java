package edu.litmus.compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
    BufferedReader reader;
    String currentCommand;

    Parser(String fileName) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(fileName));
        advance();
    }

    public Boolean hasMoreCommands() {
        return currentCommand != null;
    }

    public void advance() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("//")) {
                    int commentIndex = line.indexOf("//");
                    if (commentIndex != -1) {
                        line = line.substring(0, commentIndex).trim();
                    }
                    currentCommand = line;
                    return;
                }
            }
            currentCommand = null;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    };

    public String returnCurrLine() {
        return currentCommand;
    }

    public CommandType commandType() {
        if (currentCommand != null) {
            String[] tokenizedCommand = currentCommand.split(" ");
            if (tokenizedCommand.length == 1) {
                return CommandType.C_ARITHEMTIC;
            }
            String command = tokenizedCommand[0];
            switch (command) {
                case "push":
                    return CommandType.C_PUSH;
                case "pop":
                    return CommandType.C_POP;
            }
        }
        return CommandType.ERROR;

    }

    public String arg0() {
        String[] tokenizedCommand = currentCommand.split(" ");
        return tokenizedCommand[0];
    }

    public String arg1() {
        String[] tokenizedCommand = currentCommand.split(" ");
        return tokenizedCommand[1];
    }

    public String arg2() {
        String[] tokenizedCommand = currentCommand.split(" ");
        return tokenizedCommand[2];
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("Error closing the file: " + e.getMessage());
        }
    }
}


