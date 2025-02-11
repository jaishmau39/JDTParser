# Java AST Parser

This project utilizes the Eclipse JDT (Java Development Tools) AST (Abstract Syntax Tree) parser to analyze Java source files. It extracts method declarations, method calls, variable declarations, and exception handling structures.

## Features:
- Parses Java files and extracts method declarations, method calls, and variables.
- Analyzes exceptions in catch clauses.
- Outputs the return types, method names, parameters, and method calls associated with each method.
- Tracks variables and their invocation of methods using a visitor pattern.

## Requirements:
- Java Development Kit (JDK) version 8 or above.
- Eclipse JDT libraries for AST parsing.
- Apache Commons IO library for file handling.

## Usage:
1. Clone the repository.
   ```
   git clone https://github.com/jaishmau39/JDTParser.git
   cd JDTParser
   ```
3. Ensure the required dependencies are included in your project.
4. Modify the file paths in run(), run1(), and run2() to match your Java source files.
5. Run Test.java to generate the analysis output.

## Example Outputs:
1. Method Declarations with their return types, names, and parameters.
2. Method calls and the variables they are invoked on.
3. Variables and the line numbers where they are declared.
4. Exceptions in the catch clauses of the code.
