#!/bin/bash

printf "Running benchmarks.....\n\n"

cd benchmarks

printf "Running Clang with No Optimizations.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "clang no-optimization time for $file:\n"
		gcc -w -S -O0 -emit-llvm $file/$file.c
		clang $file.ll
		if [ -e a.out ];
		then 
			chmod 777 $file/input.longer
			time ./a.out < $file/input.longer > /dev/null 2>&1
		fi
			[ -e $file.ll ] && rm $file.ll
			[ -e $file.s ] && rm $file.s
		printf "\n"
	fi
done

printf "Running Clang with Optimizations.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "clang optimization time for $file:\n"
		gcc -w -S -O3 -emit-llvm $file/$file.c
		clang $file.ll
		if [ -e a.out ];
		then 
			chmod 777 $file/input.longer
			time ./a.out < $file/input.longer > /dev/null 2>&1
		fi
			[ -e $file.ll ] && rm $file.ll
			[ -e $file.s ] && rm $file.s
		printf "\n"
	fi
done

printf "Running LLVM Stack-Based Benchmarks.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "$file test:\n\n"

		java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler $file/$file.mini -llvm -stack
		if [ -e $file.ll ];
		then 
			printf "word count for stack-based: "
			wc -l $file.ll
			printf "\n\n"
			clang $file.ll ../src/main/java/mini/read_helper.c
			if [ -e a.out ];
			then 
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			[ -e $file.ll ] && rm $file.ll
			[ -e $file.s ] && rm $file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done
printf "Running LLVM SSA Benchmark Tests.....\n"
for file in * ; 
do 
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then 
		printf "$file test:\n\n"

		java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler -llvm
		if [ -e $file.ll ];
		then 
			printf "word count for ssa: "
			wc -l $file.ll
			printf "\n\n"
			clang $file.ll ../src/main/java/mini/read_helper.c
			if [ -e a.out ];
			then 
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			[ -e $file.ll ] && rm $file.ll
			[ -e $file.s ] && rm $file.s
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
		printf "$file test:\n\n"

		java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler -uce -llvm -lvn
		if [ -e $file.ll ];
		then 

			clang $file.ll ../src/main/java/mini/read_helper.c
			if [ -e a.out ];
			then 
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			[ -e $file.ll ] && rm $file.ll
			[ -e $file.s ] && rm $file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

exit