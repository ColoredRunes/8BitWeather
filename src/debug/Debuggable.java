package debug;

import java.util.HashMap;

public abstract class Debuggable {
    // public static ArrayList<Debuggable> components = new ArrayList<>();
    public static HashMap<String,Debuggable> hashmap = new HashMap<>();
    public abstract void printDebug();
    protected Debuggable(String o_ClassName_String){
        hashmap.put(o_ClassName_String, this);
    }
    public static void printAllDebuggingInfo(){
        for (String s : hashmap.keySet()) {
            System.err.println("____"+s+"____");
            hashmap.get(s).printDebug();
        }   
    }
}
