// JavaLine 1 <== SourceLine 0
package simprog;
// Simula-2.0 Compiled at Tue Nov 03 09:50:36 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc08_Block0_Block8_Block16 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=3, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 17
    public int intr=0;
    // JavaLine 11 <== SourceLine 18
    public float rea=0.0f;
    // Normal Constructor
    public adHoc08_Block0_Block8_Block16(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 22 <== SourceLine 20
        intr=10;
        // JavaLine 24 <== SourceLine 21
        rea=10.0f;
        // JavaLine 26 <== SourceLine 22
        RTS_BASICIO.sysout().outtext(CONC(CONC(CONC(new RTS_TXT("Enter block: "),RTS_ENVIRONMENT.edit(23)),new RTS_TXT(", intr=")),RTS_ENVIRONMENT.edit(intr)));
        RTS_BASICIO.sysout().outimage();
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc08.sim","SubBlock Block16",1,0,9,17,11,18,22,20,24,21,26,22,31,0);
} // End of SubBlock
