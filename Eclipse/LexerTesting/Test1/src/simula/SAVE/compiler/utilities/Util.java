package simula.compiler.utilities;

public class Util {
    public static void warning(String s) {
    	System.out.println("WARNING: "+s);
    }

    public static void TRACE(String s) {
    	System.out.println("TRACE: "+s);
    }

    public static void error(String s) {
    	System.out.println("ERROR: "+s);
    }

    public static void ASSERT(boolean test, String s) {
    	if(!test) error("Assertion FAILED: "+s);
    }

    public static void assertEquals(String msg, String s1, String s2) {
    	boolean ok = true;
    	if(s1 == null) {
    		if(s2 != null) ok = false;
    	} else {
    		ok = s1.equals(s2);
    	}
    	if(!ok) error("Assertion FAILED: "+msg);
    }

    public static boolean equals(String name, String end) {
        return false;
    }

    public static void IERR() {
    	System.out.println("INTERNAL ERROR: ");
    	Thread.dumpStack();
    	System.exit(-1);
    }

    public static void IERR(String msg) {
    	System.out.println("INTERNAL ERROR: " + msg);
    	Thread.dumpStack();
    	System.exit(-1);
    }
}
