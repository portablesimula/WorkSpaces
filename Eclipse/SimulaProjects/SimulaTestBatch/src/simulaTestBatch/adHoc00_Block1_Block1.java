// JavaLine 1 <== SourceLine 4
package simulaTestBatch;
// Simula-2.0 Compiled at Thu Apr 09 10:28:54 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_Block1 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=2, firstLine=4, lastLine=7, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 5
    public int block5=0;
    // Normal Constructor
    public adHoc00_Block1_Block1(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 20 <== SourceLine 4
        RTS_BASICIO.sysout().outtext(new RTS_TXT("In Block 5"));
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SubBlock Block1",1,4,9,5,20,4,25,7);
} // End of SubBlock
