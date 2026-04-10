// JavaLine 1 <== SourceLine 10
package simulaTestBatch;
// Simula-2.0 Compiled at Sun Apr 05 12:59:45 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_Block1_Block6 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=3, firstLine=10, lastLine=11, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 9
    public int block11=0;
    // Normal Constructor
    public adHoc00_Block1_Block1_Block6(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 20 <== SourceLine 10
        RTS_BASICIO.sysout().outtext(new RTS_TXT("In Block 11"));
        ;
        RTS_BASICIO.sysout().outimage();
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SubBlock Block6",1,10,9,9,20,10,27,11);
} // End of SubBlock
