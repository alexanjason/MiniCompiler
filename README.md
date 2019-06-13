# MiniCompiler

Command Line Options:
-llvm		Generate LLVM file instead of ARM32 file
-stack 		Stack-Based instead of Register-Based
-regalloc	Replace virtual registers with real registers
-uce		Run LLVM level Useless Code Elimination Optimization
-sscp 		Run LLVM level Simple Sparse Constant Propagation Optimization
-lvn 		Run LLVM level Local Value Numbering Optimization

Notes:
* generated LLVM is compliant with version 10.0.0 (MacOSX compatible)
* If run on MacOSX, some benchmarks have 32 bit number overflow issues(OptimizationBenchmark, hanoi_benchmark, killerBubbles, bert, stats)
* Fibonacci benchmark has a branch to an empty block we didn't have time to debug
* SSCP has a bug where Phi instructions that reference each other cause problems (SSCP works for: BenchMarkishTopics, binaryConverter, programBreaker, biggest, mixed, hailstone, fact_sum)
* Regalloc generates an ARM32 file that can be assembled with Clang but the output when ran still has bugs

How to run:

java -cp ${CLASSPATH}:<path_to_antlr>:<path_to_classes> mini.MiniCompiler <filename> <flags>

Example paths:
<path_to_antlr> = /Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar
<path_to_classes> = /Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes

Unit tests in test folder can be used to run the compiler against various benchmarks with various configurations. 
Also see bash scripts in this repo for how we ran stats tests.
