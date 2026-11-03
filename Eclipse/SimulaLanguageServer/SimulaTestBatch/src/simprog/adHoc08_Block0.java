package simprog;
// Simula-2.0 Compiled at Tue Nov 03 09:50:36 CET 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc08_Block0 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=1, firstLine=0, lastLine=0, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 8 <== SourceLine 2
    public int isum=0;
    // JavaLine 10 <== SourceLine 3
    public float rsum=0.0f;
    // JavaLine 12 <== SourceLine 4
    public RTS_TXT txt=null;
    // Normal Constructor
    public adHoc08_Block0(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 23 <== SourceLine 6
        RTS_BASICIO.sysout().outtext(CONC(CONC(CONC(new RTS_TXT("Enter block: "),RTS_ENVIRONMENT.edit(7)),new RTS_TXT(", isum=")),RTS_ENVIRONMENT.edit(isum)));
        RTS_BASICIO.sysout().outimage();
        // JavaLine 26 <== SourceLine 8
        new adHoc08_Block0_Block8((_CUR))._STM();
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc08.sim","SubBlock Block0",8,2,10,3,12,4,23,6,26,8,30,0);
} // End of SubBlock
