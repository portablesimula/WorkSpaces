package testing.util;

public class Util {
    public static void warning(String s) {
    	IO.println("WARNING: "+s);
    }

    public static void TRACE(String s) {
    	IO.println("TRACE: "+s);
    }

    public static void error(String s) {
    	IO.println("ERROR: "+s);
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
    	IO.println("INTERNAL ERROR: ");
    	System.exit(-1);
    }

    public static void IERR(String msg) {
    	IO.println("INTERNAL ERROR: " + msg);
    	System.exit(-1);
    }
}
