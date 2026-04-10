package simulaTestBatch;
// Simula-2.0 Compiled at Thu Apr 09 10:28:54 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=1, lastLine=9, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 2
    public int isum=0;
    // Normal Constructor
    public adHoc00_Block1(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 19 <== SourceLine 1
        new adHoc00_Block1_Block1((_CUR))._STM();
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SubBlock Block1",8,2,19,1,24,9);
} // End of SubBlock
