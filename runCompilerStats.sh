#!/bin/bash

printf "Running benchmarks.....\n\n"

cd benchmarks

printf "Running LLVM SSA Benchmark Tests.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "$file:\n\n"

		java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler $file/$file.mini -llvm
		if [ -e $file/$file.ll ];
		then 
			printf "word count for ssa: "
			wc -l $file/$file.ll
			printf "\n\n"
			clang $file/$file.ll ../src/main/resources/read_util.c
			if [ -e a.out ];
			then 
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			[ -e $file/$file.ll ] && rm $file/$file.ll
			[ -e $file/$file.s ] && rm $file/$file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

printf "Running LLVM SSA Benchmark Tests with Optimizations.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "$file:\n\n"

		java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler $file/$file.mini -uce -llvm -lvn
		if [ -e $file/$file.ll ];
		then 

			clang $file/$file.ll ../src/main/resources/read_util.c
			if [ -e a.out ];
			then 
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			[ -e $file/$file.ll ] && rm $file/$file.ll
			[ -e $file/$file.s ] && rm $file/$file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

exit