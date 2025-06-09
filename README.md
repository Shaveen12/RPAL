# RPAL Project - Makefile Usage

This project provides a Makefile to compile and run the RPAL interpreter written in Java. The Makefile supports compiling all source files, running the interpreter on an input file, and printing the Abstract Syntax Tree (AST) along with the result.

## Prerequisites
- Java (JDK 8 or higher)
- macOS or Linux (uses `find` for cleaning)

## Makefile Targets

### 1. Compile All Java Files
```
make all
```
Compiles all `.java` files in the project and outputs `.class` files to the current directory and subdirectories.

### 2. Run the Interpreter
```
make run file=filename.txt
```
Runs the interpreter on the specified input file. Only the final result (output value) is printed.

### 3. Run and Print AST
```
make run-ast file=filename.txt
```
Runs the interpreter on the specified input file and prints both the output value and the Abstract Syntax Tree (AST).

### 4. Clean Compiled Files
```
make clean
```
Removes all `.class` files recursively from the project directory and subdirectories.

## Direct Java Usage
After compiling with `make all`, you can also run the interpreter directly:
```
java myrpal filename.txt
```
Or to print both the value and AST:
```
java myrpal -ast filename.txt
```

## Notes
- If no file is specified, the program defaults to `test.txt` (which must exist in the current directory).
- The `-ast` switch prints both the output value and the AST.


## Example
```
make all
make run file=test.txt
make run-ast file=test.txt
```


