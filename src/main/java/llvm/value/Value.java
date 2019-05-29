package llvm.value;

import llvm.inst.Instruction;
import llvm.type.Type;

import java.util.List;

public interface Value {

    Type getType();

    String getString();

    String getId();

    void addDef(llvm.inst.Instruction def);

    void addUse(llvm.inst.Instruction use);

    Instruction getDef();

    List<Instruction> getUses();
}
