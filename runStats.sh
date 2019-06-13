#!/bin/bash

printf "Running benchmarks.....\n\n"

cd inputFiles

printf "Running Clang with No Optimizations.....\n"
for file in * ;
do
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then
		printf "clang no-optimization time for $file:\n"
		# gcc -w -S -O0 -emit-llvm $file/$file.c
		clang $file/$file.c -w -S -O0 -emit-llvm
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
		# gcc -w -S -O3 -emit-llvm $file/$file.c
		clang $file/$file.c -w -S -O3 -emit-llvm
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

printf "Running register-based-llvm-no-op .....\n"
for file in * ;
do
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then
		printf "$file test:\n\n"

		# java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler -uce -llvm -lvn
		if [ -e "../register-based-llvm-no-op/$file.ll" ];
		then

			clang "../register-based-llvm-no-op/$file.ll" read_util.c
			if [ -e a.out ];
			then
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			# [ -e $file.ll ] && rm $file.ll
			# [ -e $file.s ] && rm $file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

printf "Running register-based-llvm-with-sscp-op .....\n"
for file in * ;
do
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then
		printf "$file test:\n\n"

		# java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler -uce -llvm -lvn
		if [ -e "../register-based-llvm-w-sscp-op/$file.ll" ];
		then

			clang "../register-based-llvm-w-sscp-op/$file.ll" read_util.c
			if [ -e a.out ];
			then
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			# [ -e $file.ll ] && rm $file.ll
			# [ -e $file.s ] && rm $file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

printf "Running register-based-llvm-with-op .....\n"
for file in * ;
do
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then
		printf "$file test:\n\n"

		# java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler -uce -llvm -lvn
		if [ -e "../register-based-llvm-w-op/$file.ll" ];
		then

			clang "../register-based-llvm-w-op/$file.ll" read_util.c
			if [ -e a.out ];
			then
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			# [ -e $file.ll ] && rm $file.ll
			# [ -e $file.s ] && rm $file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

printf "Running stack-based-llvm-no-op .....\n"
for file in * ;
do
	[ -e a.out ] && rm a.out

	if [ $file != "a.out" ];
	then
		printf "$file test:\n\n"

		# java -cp ${CLASSPATH}:/Users/alexadrenick/.m2/repository/org/antlr/antlr4-runtime/4.7.1/antlr4-runtime-4.7.1.jar:/Users/alexadrenick/Documents/*Poly/*Spring19/CSC431/MiniCompiler/target/classes mini.MiniCompiler -uce -llvm -lvn
		if [ -e "../stack-based-llvm-no-op/$file.ll" ];
		then

			clang "../stack-based-llvm-no-op/$file.ll" read_util.c
			if [ -e a.out ];
			then
				chmod 777 $file/input.longer
				time ./a.out < $file/input.longer > /dev/null 2>&1
			fi
			# [ -e $file.ll ] && rm $file.ll
			# [ -e $file.s ] && rm $file.s
		fi
		printf "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n"
	fi
done

exit