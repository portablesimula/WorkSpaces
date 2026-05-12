// JavaLine 1 <== SourceLine 14
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:23:32 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class adHoc00_Block1_SimulationBegin_p extends RTS_Process {
    // ClassDeclaration: Kind=9, BlockLevel=2, PrefixLevel=3, firstLine=14, lastLine=15, hasLocalClasses=false, System=false, detachUsed=false
public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    public int p3_i;
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_Block1_SimulationBegin_p(RTS_RTObject staticLink,int sp3_i) {
        super(staticLink);
        // Parameter assignment to locals
        this.p3_i = sp3_i;
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc00_Block1_SimulationBegin_p _STM() {
        // BEGIN Linkage INNER PART
        // BEGIN Link INNER PART
        // JavaLine 24 <== SourceLine -23
        detach(); // Process'detach
        // BEGIN Process INNER PART
        // JavaLine 27 <== SourceLine 14
        // BEGIN p INNER PART
        // ENDOF p INNER PART
        // ENDOF Process INNER PART
        // JavaLine 31 <== SourceLine -23
        terminate(); // Process'terminate
        // ENDOF Link INNER PART
        // ENDOF Linkage INNER PART
        EBLK();
        return(this);
    } // End of Class Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Class p",1,14,24,-23,27,14,31,-23,37,15);
} // End of Class
