# Trinity VM


## Introduction
![Overview] (./overview.jpg)
The project implements a virtual machine which executs bytecode instructions, and an interperter that accepts scheme code, simple imperative code and bytecode. All the scheme and imperative code are first compiled to bytecode before being executed.


There are both Java and Scala implementations, note that the Java version's interpretor only supports scheme code.

## Current Progress
###1. Scheme Interpreter
* Arthmetic:   (+ 1 2)
* Boolean:     (< x 10)
* Definition:  (define var exp)
* Conditional: (if test conseq alt) 
* Procedure:   (lambda (var...) exp) 
* Sequencing:  (begin exp...)
* List
* car, cdr
* Closure
* continuation(call/cc)

###2. Impretaive Language Interpreter

Statement Type              | Example
---------------------- | --------------------------------
Boolean                | 1 < 2
Arthmetic              | 1 + 2, a * b
Assignment             | int a = 3
Function Declaration   | def add(x, y) {x + y}
Fuction Call           | add(a, 4)
Conditional            | if (a > 10) {a + 3} else {a - 3}

###3. Bytecode Interpreter


Only PUSH, ADD, SUB, MUL, DIV are supported for now.


## Future Plans
TBD...

\* The project was first written for CS653 class at USFCA.