// JavaLine 1 <== SourceLine 0
package simprog;
// Simula-2.0 Compiled at Tue Nov 03 09:50:36 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc08_Block0_Block8 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=2, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 9
    public int intr=0;
    // JavaLine 11 <== SourceLine 10
    public float rea=0.0f;
    // Normal Constructor
    public adHoc08_Block0_Block8(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 22 <== SourceLine 12
        intr=1;
        // JavaLine 24 <== SourceLine 13
        rea=1.0f;
        // JavaLine 26 <== SourceLine 14
        RTS_BASICIO.sysout().outtext(CONC(CONC(CONC(new RTS_TXT("Enter block: "),RTS_ENVIRONMENT.edit(15)),new RTS_TXT(", intr=")),RTS_ENVIRONMENT.edit(intr)));
        RTS_BASICIO.sysout().outimage();
        // JavaLine 29 <== SourceLine 16
        new adHoc08_Block0_Block8_Block16((_CUR))._STM();
        // JavaLine 31 <== SourceLine 25
        rea=0.0f;
        // JavaLine 33 <== SourceLine 27
        new adHoc08_Block0_Block8_Block27((_CUR))._STM();
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc08.sim","SubBlock Block8",1,0,9,9,11,10,22,12,24,13,26,14,29,16,31,25,33,27,37,0);
} // End of SubBlock
