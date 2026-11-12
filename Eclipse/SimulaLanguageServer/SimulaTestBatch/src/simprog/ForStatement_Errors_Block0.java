package simprog;
// Simula-2.0 Compiled at Thu Nov 12 09:38:40 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class ForStatement_Errors_Block0 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=0, lastLine=0, hasLocalClasses=true, System=true
public boolean isQPSystemBlock() { return(true); }
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 5
    public ForStatement_Errors_Block0_C x=null;
    // Normal Constructor
    public ForStatement_Errors_Block0(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        x=new ForStatement_Errors_Block0_C((_CUR))._STM(); {
            ;
        }
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaErrors/statement/ForStatement_Errors.sim","SubBlock Block0",9,5,25,0);
} // End of SubBlock
