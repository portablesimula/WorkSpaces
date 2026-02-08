package simulaTestBatch;
// Simula-2.0 Compiled at Mon Dec 29 10:50:12 CET 2025
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block2_Block3 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=2, lastLine=8, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 3
    public RTS_Head liste=null;
    // JavaLine 10 <== SourceLine 4
    public RTS_Link elt=null;
    // Normal Constructor
    public adHoc00_Block2_Block3(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 21 <== SourceLine 6
        elt=liste.first();
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","SubBlock Block3",8,3,10,4,21,6,26,8);
} // End of SubBlock
