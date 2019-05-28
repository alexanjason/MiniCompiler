package llvm.value;

import llvm.type.Type;

public interface Value {

    Type getType();

    String getString();

    String getId();

}
