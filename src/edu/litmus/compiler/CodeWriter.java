package edu.litmus.compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
    BufferedWriter writer;
    String fileN;

    CodeWriter(String fileName) throws IOException {
        String[] fileNameParts = fileName.split("\\.");
        if (fileNameParts.length > 0) {
            fileName = fileNameParts[0];
            this.fileN = fileNameParts[0];
        }
        writer = new BufferedWriter(new FileWriter(fileName + ".asm"));
    }

    private static int counter = 0;

    private String getLabel(String base) {
        return base + "_" + (counter++);
    }

    public void writePush(String segment, int index) throws IOException {
        switch (segment) {
            case "constant":
                writer.write("@" + index + "\n");
                writer.write("D=A\n");
                break;
            case "local":
            case "argument":
            case "this":
            case "that": {
                String segmentPointer = getSegmentPointer(segment);
                writer.write("@" + segmentPointer + "\n");
                writer.write("D=M\n");
                writer.write("@" + index + "\n");
                writer.write("A=D+A\n");
                writer.write("D=M\n");
            }
                break;
            case "pointer":
                writer.write("@" + ((index == 0) ? "THIS" : "THAT") + "\n");
                writer.write("D=M\n");
                break;
            case "temp":
                writer.write("@" + (5 + index) + "\n");
                writer.write("D=M\n");
                break;
            case "static":
                writer.write("@" + fileN + "." + index + "\n");
                writer.write("D=M\n");
                break;
        }
        writer.write("@SP\n");
        writer.write("A=M\n");
        writer.write("M=D\n");
        writer.write("@SP\n");
        writer.write("M=M+1\n");

    }

    public void writePop(String segment, int index) throws IOException {
        switch (segment) {
            case "local":
            case "this":
            case "that":
            case "argument": {
                String segmentPointer = getSegmentPointer(segment);
                writer.write("@SP\n");
                writer.write("M=M-1\n");
                writer.write("@" + segmentPointer + "\n");
                writer.write("D=M\n");
                writer.write("@" + index + "\n");
                writer.write("D=D+A\n");
                writer.write("@R13\n");
                writer.write("M=D\n");
                writer.write("@SP\n");
                writer.write("A=M\n");
                writer.write("D=M\n");
                writer.write("@R13\n");
                writer.write("A=M\n");
                writer.write("M=D\n");
            }
                break;
            case "static": {
                writer.write("@SP\n");
                writer.write("A=M-1\n");
                writer.write("D=M\n");
                writer.write("@SP\n");
                writer.write("M=M-1\n");
                writer.write("@" + fileN + "." + index + "\n");
                writer.write("M=D\n");
            }

                break;
            case "pointer": {
                writer.write("@SP\n");
                writer.write("M=M-1\n");
                writer.write("A=M\n");
                writer.write("D=M\n");
                writer.write("@" + ((index == 0) ? "THIS" : "THAT") + "\n");
                writer.write("M=D\n");
            }
                break;
            case "temp": {
                writer.write("@SP\n");
                writer.write("A=M-1\n");
                writer.write("D=M\n");
                writer.write("@" + (5 + index) + "\n");
                writer.write("M=D\n");
                writer.write("@SP\n");
                writer.write("M=M-1\n");
            }

            default:
                break;
        }
    }

    public void writeArithmetic(String operation) {
        switch (operation) {
            case "add":
                try {
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("D=M\n");
                    writer.write("A=A-1\n");
                    writer.write("D=D+M\n");
                    writer.write("@SP\n");
                    writer.write("M=M-1\n");
                    writer.write("A=M\n");
                    writer.write("A=A-1\n");
                    writer.write("M=D\n");
                } catch (Exception e) {
                    System.out.println("Error adding");
                }
                break;
            case "sub":
                try {
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("D=-M\n");
                    writer.write("A=A-1\n");
                    writer.write("D=D+M\n");
                    writer.write("@SP\n");
                    writer.write("M=M-1\n");
                    writer.write("A=M\n");
                    writer.write("A=A-1\n");
                    writer.write("M=D\n");
                } catch (Exception e) {
                    System.out.println("Error subtracting");
                }
                break;
            case "neg":
                try {
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("M=-M\n");
                } catch (Exception e) {
                    System.out.println("Error generate negative");
                }
                break;
            case "gt":
            case "lt":
                String op = operation.substring(0, 1).toUpperCase();
                try {
                    String trueLabel = getLabel("TRUE_" + op);
                    String endLabel = getLabel("END_" + op);
                    writer.write("@SP\n");
                    writer.write("AM=M-1\n");
                    writer.write("D=M\n");
                    writer.write("A=A-1\n");
                    writer.write("D=M-D\n");
                    writer.write("@" + trueLabel + "\n");
                    writer.write("D;J" + op + "T\n");
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("M=0\n");
                    writer.write("@" + endLabel + "\n");
                    writer.write("0;JMP\n");
                    writer.write("(" + trueLabel + ")\n");
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("M=-1\n");
                    writer.write("(" + endLabel + ")\n");
                } catch (IOException e) {
                    throw new RuntimeException("Error in generating Greater or Less than", e);
                }
                break;
            case "eq":
                try {
                    String eqTrueLabel = getLabel("EQ_TRUE");
                    String eqEndLabel = getLabel("EQ_END");
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("D=M\n");
                    writer.write("A=A-1\n");
                    writer.write("D=M-D\n");
                    writer.write("@" + eqTrueLabel + "\n");
                    writer.write("D;JEQ\n");
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("A=A-1\n");
                    writer.write("M=0\n");
                    writer.write("@" + eqEndLabel + "\n");
                    writer.write("0;JMP\n");
                    writer.write("(" + eqTrueLabel + ")\n");
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("A=A-1\n");
                    writer.write("M=-1\n");
                    writer.write("(" + eqEndLabel + ")\n");
                    writer.write("@SP\n");
                    writer.write("M=M-1\n");
                } catch (Exception e) {
                    System.out.println("Error equating");
                }
                break;
            case "and":
            case "or": {
                String sym = (operation.equals("and")) ? "&" : "|";
                try {
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("D=M\n");
                    writer.write("A=A-1\n");
                    writer.write("M=D" + sym + "M\n");
                    writer.write("@SP\n");
                    writer.write("M=M-1\n");
                } catch (Exception e) {
                    System.out.println("Error in generating AND");
                }
            }
                break;
            case "not":
                try {
                    writer.write("@SP\n");
                    writer.write("A=M-1\n");
                    writer.write("M=!M\n");
                } catch (Exception e) {
                    System.out.println("Error in generating NOT");
                }
        }
    }

    private String getSegmentPointer(String segment) {
        switch (segment) {
            case "local":
                return "LCL";
            case "argument":
                return "ARG";
            case "this":
                return "THIS";
            case "that":
                return "THAT";
            default:
                throw new IllegalArgumentException("Invalid segment: " + segment);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Error closing the file: " + e.getMessage());
        }
    }

}

