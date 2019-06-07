package cfg;

import llvm.value.Value;

import java.util.HashMap;
import java.util.Map;

public class LocalValueNumbering {

    Map<String, Integer> lvnMap;
    Map<String, Value> valueMap;
    int number;

    public LocalValueNumbering()
    {
        lvnMap = new HashMap<>();
        valueMap = new HashMap<>();
        number = 0;
    }

    public boolean isInMap(String str)
    {
        return lvnMap.containsKey(str);
    }

    public void enterInMap(String str, Value v)
    {
        lvnMap.put(str, number);
        number++;
        valueMap.put(str, v);
    }

    public Value getVal(String str)
    {
        return valueMap.get(str);
    }

    public int getNumbering(String str)
    {
        return lvnMap.get(str);
    }

}
