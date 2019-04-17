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
	%_P_j = alloca %struct.B*
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
	%_P_j = alloca %struct.B*
	%f = alloca i32
	%l = alloca i32
	%k = alloca i32
	%u11 = load %struct.B*, %struct.B** @b
	%u12 = getelementptr %struct.B, %struct.B* %u10, i1 0, i32 0
	%u13 = load %struct.A*, %struct.A** %u12
	store %struct.A* %u13, %struct.A** %_retval_
	br label %LU8
LU8:
	%u9 = load %struct.A*, %struct.A** %_retval_
	ret %struct.A* %u9
}

define void @bar(%struct.B* %j, i32 %i)
{
LU9:
	%_P_j = alloca %struct.B*
	%_P_i = alloca i32
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
	%u16 = mul i32 -1, 2
	%u17 = mul i32 -1, %u16
	store i32 %u17, i32* %i
	%u18 = load i32, i32* %i
	%u19 = call i32 @g(i32 1, %struct.B* null)
	%u20 = icmp slt i32 %u18, %u19
	br i1 %u20, label %LU13, label %LU14
LU13:
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i321
LU15:
	%u21 = load i32, i32* %i
	%u22 = call i32 @g(i32 1, %struct.B* null)
	%u23 = icmp sgt i32 %u21, %u22
	br i1 %u23, label %LU16, label %LU17
LU16:
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i321
LU17:
	call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i323
LU18:
	br i1 true, label %LU19, label %LU20
LU19:
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i327
	br i1 true, label %LU19, label %LU20
LU20:
	%u24 = call *i8 @malloc(i32 0)
	%u25 = call i32 @g(i32 1, %struct.B* %u24)
	%u26 = call *i8 @malloc(i32 0)
	%u27 = call %struct.A* @f(i32 %u25, %struct.B* %u26)
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i321
	%u28 = call *i8 @malloc(i32 0)
	%u29 = call *i8 @malloc(i32 0)
	%u30 = call i32 @g(i32 1, %struct.B* %u29)
	%u31 = call void @bar(%struct.B* %u28, i32 %u30)
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i322
	%u33 = call *i8 @malloc(i32 0)
	%u34 = call i32 @g(i32 1, %struct.B* %u33)
	%u35 = call *i8 @malloc(i32 0)
	%u36 = call %struct.A* @f(i32 %u34, %struct.B* %u35)
	%u37 = getelementptr %struct.A, %struct.A* %u32, i1 0, i32 0
	%u38 = load i32, i32* %u37
	call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i32%u38
	store i32 0, i32* %_retval_
	br label %LU12
LU12:
	%u15 = load i32, i32* %_retval_
	ret i32 %u15
}

declare i8* @malloc(i32)
declare void @free(i8*)
declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
@.println = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1
@.print = private unnamed_addr constant [5 x i8] c"%ld\00", align 1
@.read = private unnamed_addr constant [4 x i8] c"%ld\00", align 1
