%struct.A = type {i32, i32, i32, %struct.A*}
%struct.B = type {%struct.A*}

@i = common global i32 0, align 8
@j = common global i32 0, align 8
@k = common global i32 0, align 8
@b = common global %struct.B* null, align 8
@bb = common global %struct.B* null, align 8
@bbb = common global %struct.B* null, align 8
@bob = common global i32 0, align 8

define i32 @g(i32 %i, %struct.B* %j)
{
L0:
	%_P_i = alloca i32
	%_P_j = alloca %struct.B*
	%g = alloca i32
	%m = alloca i32
	%k = alloca i32
	store i32 3, i32* %_retval
	br label L1
L1:
	%u0 = load i32* %_retval
	ret i32 %u0

define i32 @foo(i32 %n)
{
L2:
	%_P_n = alloca i32
	%u7 = load i32* %n
	%u8 = icmp sle i32 %u7, 0
	br i1 %u8, label L4, label L5
L4:
	store i32 1, i32* %_retval
	br label L3
L3:
	%u1 = load i32* %_retval
	ret i32 %u1
L5:
	%u2 = load i32* %n
	%u3 = load i32* %n
	%u4 = sub i32 %u3, 1
	%u5 = call i32 @foo(i32 %u4)
	%u6 = mul i32 %u2, %u5
	store i32 %u6, i32* %_retval
	br label L3
L3:
	%u1 = load i32* %_retval
	ret i32 %u1
L6:

define %struct.A* @f(i32 %i, %struct.B* %j)
{
L7:
	%_P_i = alloca i32
	%_P_j = alloca %struct.B*
	%f = alloca i32
	%l = alloca i32
	%k = alloca i32
	%u14 = load %struct.B** %b
	%u13 = load %struct.B** %u14
	%u15 = getelementptr %struct.B* %u13, i1 0, i320
	%u16 = load %struct.A** %u15
	%u12 = load %struct.A** %u16
	%u17 = getelementptr %struct.A* %u12, i1 0, i323
	%u18 = load %struct.A** %u17
	%u11 = load %struct.A** %u18
	%u19 = getelementptr %struct.A* %u11, i1 0, i323
	%u20 = load %struct.A** %u19
	%u10 = load %struct.A** %u20
	%u21 = getelementptr %struct.A* %u10, i1 0, i320
	%u22 = load i32* %u21
	store i32 7, i32* %u22
	%u27 = load %struct.B** %b
	%u26 = load %struct.B** %u27
	%u28 = getelementptr %struct.B* %u26, i1 0, i320
	%u29 = load %struct.A** %u28
	%u25 = load %struct.A** %u29
	%u30 = getelementptr %struct.A* %u25, i1 0, i323
	%u31 = load %struct.A** %u30
	%u24 = load %struct.A** %u31
	%u32 = getelementptr %struct.A* %u24, i1 0, i323
	%u33 = load %struct.A** %u32
	%u23 = load %struct.A** %u33
	%u34 = getelementptr %struct.A* %u23, i1 0, i323
	%u35 = load %struct.A** %u34
	store %struct.A* %u35, %struct.A** %_retval
	br label L8
L8:
	%u9 = load %struct.A** %_retval
	ret %struct.A* %u9

define void @bar(%struct.B* %j, i32 %i)
{
L9:
	%_P_j = alloca %struct.B*
	%_P_i = alloca i32

define i32 @main()
{
L11:
	%a = alloca %struct.A*
	%i = alloca i32
	%j = alloca i32
	%k = alloca i32
	%b = alloca i32
	%h = alloca i32
	%u38 = mul i32 -1, 2
	%u39 = mul i32 -1, %u38
	store i32 %u39, i32* %i
	%u40 = load i32* %i
	%u41 = call i32 @g(i32 1, %struct.B* null)
	%u42 = icmp slt i32 %u40, %u41
	br i1 %u42, label L13, label L14
L13:
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i321
L14:
L15:
	%u43 = load i32* %i
	%u44 = call i32 @g(i32 1, %struct.B* null)
	%u45 = icmp sgt i32 %u43, %u44
	br i1 %u45, label L16, label L17
L16:
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i321
L17:
	call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i323
L18:
	br i1 true, label L19, label L20
L19:
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i327
	br i1 true, label L19, label L20
L20:
	%u46 = call *i8 @malloc(i32 0)
	%u47 = call i32 @g(i32 1, %struct.B* %u46)
	%u48 = call *i8 @malloc(i32 0)
	%u49 = call %struct.A* @f(i32 %u47, %struct.B* %u48)
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i321
	%u50 = call *i8 @malloc(i32 0)
	%u51 = call *i8 @malloc(i32 0)
	%u52 = call i32 @g(i32 1, %struct.B* %u51)
	%u53 = call void @bar(%struct.B* %u50, i32 %u52)
	call i32 (i8*, ...)* @print(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i322
	%u55 = call *i8 @malloc(i32 0)
	%u56 = call i32 @g(i32 1, %struct.B* %u55)
	%u57 = call *i8 @malloc(i32 0)
	%u58 = call %struct.A* @f(i32 %u56, %struct.B* %u57)
	%u54 = load %struct.A** %u58
	%u59 = getelementptr %struct.A* %u54, i1 0, i320
	%u60 = load i32* %u59
	call i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), i32%u60
	store i32 0, i32* %_retval
	br label L12
L12:
	%u37 = load i32* %_retval
	ret i32 %u37

declare i8* @malloc(i32)
declare void @free(i8*)
declare i32 @printf(i8*, ...)
declare i32 @scanf(i8*, ...)
@.println = private unnamed_addr constant [5 x i8] c"%ld A ", align
@.print = private unnamed_addr constant [5 x i8] c"%ld  ", align 1
@.read = private unnamed_addr constant [4 x i8] c"%ld ", align 1
