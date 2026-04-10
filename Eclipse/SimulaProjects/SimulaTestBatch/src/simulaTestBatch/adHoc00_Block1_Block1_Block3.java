// JavaLine 1 <== SourceLine 13
package simulaTestBatch;
// Simula-2.0 Compiled at Mon Apr 06 10:57:18 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_Block1_Block3 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=3, firstLine=13, lastLine=14, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 12
    public int block11=0;
    // Normal Constructor
    public adHoc00_Block1_Block1_Block3(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 20 <== SourceLine 13
        RTS_BASICIO.sysout().outtext(new RTS_TXT("In Block 11"));
        ;
        RTS_BASICIO.sysout().outimage();
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SubBlock Block3",1,13,9,12,20,13,27,14);
} // End of SubBlock
