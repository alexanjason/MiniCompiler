#!/bin/bash

printf "Running benchmarks.....\n\n"

cd benchmarksSSCP

printf "Running LLVM SSA Benchmark Tests.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "$file:\n\n"

		java -cp ${CLASSPATH}:/Users/JasonComputer/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/JasonComputer/Documents/CompSciStuff/cpe431/MiniCompiler/target/classes mini.MiniCompiler $file/$file.mini -llvm -uce -lvn -sscp
		if [ -e $file/$file.ll ];
		then 
			printf "word count for ssa: "
			wc -l $file/$file.ll
			printf "\n\n"
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

exit