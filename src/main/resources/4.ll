%struct.A = type {i32, i32, i32, %struct.A*}
%struct.B = type {%struct.A*}

@i = common global i32 0, align 4
@j = common global i32 0, align 4
@k = common global i32 0, align 4
@b = common global %struct.B* null, align 4
@bb = common global %struct.B* null, align 4
@bbb = common global %struct.B* null, align 4
@bob = common global i32 0, align 4

define i32 @g(i32 %i, %struct.B* %j)
{
LU0:
	%_retval_ = alloca i32
	%_P_i = alloca i32
	store i32 %i, i32* %_P_i
	%_P_j = alloca %struct.B*
	store %struct.B* %j, %struct.B** %_P_j
	%g = alloca i32
	%m = alloca i32
	%k = alloca i32
	store i32 3, i32* %_retval_
	br label %LU1
LU1:
	%u0 = load i32, i32* %_retval_
	ret i32 %u0
}

define i32 @foo(i32 %n)
{
LU2:
	%_retval_ = alloca i32
	%_P_n = alloca i32
	store i32 %n, i32* %_P_n
	%u7 = load i32, i32* %_P_n
	%u8 = icmp sle i32 %u7, 0
	br i1 %u8, label %LU4, label %LU5
LU4:
	store i32 1, i32* %_retval_
	br label %LU3
LU5:
	%u2 = load i32, i32* %_P_n
	%u3 = load i32, i32* %_P_n
	%u4 = sub i32 %u3, 1
	%u5 = call i32 @foo(i32 %u4)
	%u6 = mul i32 %u2, %u5
	store i32 %u6, i32* %_retval_
	br label %LU3
LU3:
	%u1 = load i32, i32* %_retval_
	ret i32 %u1
}

define %struct.A* @f(i32 %i, %struct.B* %j)
{
LU7:
	%_retval_ = alloca %struct.A*
	%_P_i = alloca i32
	store i32 %i, i32* %_P_i
	%_P_j = alloca %struct.B*
	store %struct.B* %j, %struct.B** %_P_j
	%f = alloca i32
	%l = alloca i32
	%k = alloca i32
	%u10 = load %struct.B*, %struct.B** @b
	%u11 = getelementptr %struct.B, %struct.B* %u10, i1 0, i32 0
	%u12 = load %struct.A*, %struct.A** %u11
	%u13 = getelementptr %struct.A, %struct.A* %u12, i1 0, i32 3
	%u14 = load %struct.A*, %struct.A** %u13
	%u15 = getelementptr %struct.A, %struct.A* %u14, i1 0, i32 3
	%u16 = load %struct.A*, %struct.A** %u15
	%u17 = getelementptr %struct.A, %struct.A* %u16, i1 0, i32 0
	store i32 7, i32* %u17
	%u18 = load %struct.B*, %struct.B** @b
	%u19 = getelementptr %struct.B, %struct.B* %u18, i1 0, i32 0
	%u20 = load %struct.A*, %struct.A** %u19
	%u21 = getelementptr %struct.A, %struct.A* %u20, i1 0, i32 3
	%u22 = load %struct.A*, %struct.A** %u21
	%u23 = getelementptr %struct.A, %struct.A* %u22, i1 0, i32 3
	%u24 = load %struct.A*, %struct.A** %u23
	%u25 = getelementptr %struct.A, %struct.A* %u24, i1 0, i32 3
	%u26 = load %struct.A*, %struct.A** %u25
	store %struct.A* %u26, %struct.A** %_retval_
	br label %LU8
LU8:
	%u9 = load %struct.A*, %struct.A** %_retval_
	ret %struct.A* %u9
}

define void @bar(%struct.B* %j, i32 %i)
{
LU9:
	%_P_j = alloca %struct.B*
	store %struct.B* %j, %struct.B** %_P_j
	%_P_i = alloca i32
	store i32 %i, i32* %_P_i
	br label %LU10
LU10:
	ret void
}

define i32 @main()
{
LU11:
	%_retval_ = alloca i32
	%a = alloca %struct.A*
	%i = alloca i32
	%j = alloca i32
	%k = alloca i32
	%b = alloca i32
	%h = alloca i32
	%u29 = mul i32 -1, 2
	%u30 = mul i32 -1, %u29
	store i32 %u30, i32* %i
	%u31 = load i32, i32* %i
	%u32 = call i32 @g(i32 1, %struct.B* null)
	%u33 = icmp slt i32 %u31, %u32
	br i1 %u33, label %LU13, label %LU15
LU13:
	call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), i32 1)
	br label %LU15
LU15:
	%u34 = load i32, i32* %i
	%u35 = call i32 @g(i32 1, %struct.B* null)
	%u36 = icmp sgt i32 %u34, %u35
	br i1 %u36, label %LU16, label %LU17
LU16:
	call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), i32 1)
	br label %LU18
LU17:
	call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), i32 3)
	br label %LU18
LU18:
	call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), i32 1)
	call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.println, i32 0, i32 0), i32 2)
	store i32 0, i32* %_retval_
	br label %LU12
LU12:
	%u28 = load i32, i32* %_retval_
	ret i32 %u28
}

declare i8* @malloc(i32)
declare void @free(i8*)
declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
@.println = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1
@.print = private unnamed_addr constant [5 x i8] c"%ld \00", align 1
@.read = private unnamed_addr constant [4 x i8] c"%ld\00", align 1
