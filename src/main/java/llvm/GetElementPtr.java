package llvm;

public class GetElementPtr implements Instruction {

    // <result> = getelementptr <ty>* <ptrval>, i1 0, i32 <index>

    //    %u40 = getelementptr %struct.foo* %u10, i1 0, i32 2
    //           getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0)

    private Value result;
    private Type typePtr; //TODO ...
    private Type ptrVal; //TODO: should this be Type or Value
    private Type initType; //TODO ...
    private Integer initValue;
    private Type indexType; //TODO ...
    private Integer index; //offset

    public GetElementPtr(Value result, Type typePtr, Type ptrVal, Type initType, Integer initValue, Type indexType, Integer index) {
        this.result = result;
        this.typePtr = typePtr;
        this.ptrVal = ptrVal;
        this.initType = initType;
        this.initValue = initValue;
        this.indexType = indexType;
        this.index = index;
    }

    public String getString()
    {
        return (result.getString() + " = getelementptr " + typePtr.getString() + "*" + ptrVal.toString() + ", "
                + initType.getString() + " " + initValue + ", " + indexType.getString() + " " + index);
    }
}
