# Hack Machine Code to Assembly Compiler

This project is a Hack Machine code to assembly compiler written in Java. It translates Hack VM commands into Hack assembly code.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Error Handling](#error-handling)
- [Logging](#logging)
- [Known Problems](#known-problems)
- [Contributing](#contributing)
- [License](#license)

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/realRudraP/HackCompiler
    ```
2. Navigate to the project directory:
    ```sh
    cd HackCompiler
    ```
3. Compile the Java files:
    ```sh
    javac -d bin src/edu/litmus/compiler/*.java
    ```

## Usage

To run the compiler, use the following command:
```sh
java -cp bin edu.litmus.compiler.Main <inputfile>
```
Replace `<inputfile>` with the path to your Hack VM code file.

## Error Handling

The compiler handles various types of errors:
- **IOException**: Issues with file input/output.
- **IllegalArgumentException**: No input file provided.
- **SyntaxErrorException**: Unknown command type encountered.
- **Exception**: Any other unknown errors.

## Logging

Error messages are printed to the console for easy debugging:
- `IOException encountered: <message>`
- `Argument Error: <message>`
- `Syntax Error: <message>`
- `Unknown Error: <message>`

## Known Problems

- **ASM File Creation on Error**: An `.asm` file is created even when there is an error in the input file. This issue is known and will be addressed in future updates.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
```