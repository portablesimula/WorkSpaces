package simprog;
// Simula-2.0 Compiled at Fri Nov 06 12:53:08 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class ClassDeclarationErrors_Block0 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=0, lastLine=0, hasLocalClasses=true, System=true
public boolean isQPSystemBlock() { return(true); }
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 1
    public int G=0;
    // Normal Constructor
    public ClassDeclarationErrors_Block0(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 20 <== SourceLine 8
        if(_VALUE(false)) {
            G=44;
        }
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaSemErrors/ClassDeclarationErrors.sim","SubBlock Block0",9,1,20,8,26,0);
} // End of SubBlock
