package simprog;
// Simula-2.0 Compiled at Mon Nov 09 15:43:28 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class AdHoc00_Block0 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // Normal Constructor
    public AdHoc00_Block0(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 17 <== SourceLine 3
        ((AdHoc00_Block0_P)(_CUR))._RESULT=66;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/expression/AdHoc00.sim","SubBlock Block0",17,3,21,0);
} // End of SubBlock
