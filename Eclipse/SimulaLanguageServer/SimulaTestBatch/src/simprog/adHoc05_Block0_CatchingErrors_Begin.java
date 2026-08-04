// JavaLine 1 <== SourceLine 3
package simprog;
// Simula-2.0 Compiled at Tue Aug 04 08:32:08 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc05_Block0_CatchingErrors_Begin extends RTS_CatchingErrors {
    // PrefixedBlockDeclaration: Kind=10, BlockLevel=1, firstLine=3, lastLine=3, hasLocalClasses=false, System=false, detachUsed=false
    // Declare parameters as attributes
    // Declare locals as attributes
    public RTS_Inbytefile bf_1=null;
    public RTS_PRCQNT onError_0() { return(new RTS_PRCQNT(this,adHoc05_Block0_CatchingErrors_Begin_onError.class)); }
    // Normal Constructor
    public adHoc05_Block0_CatchingErrors_Begin(RTS_RTObject staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        // Declaration Code
    }
    // Class Statements
    @Override
    public adHoc05_Block0_CatchingErrors_Begin _STM() {
        // JavaLine 21 <== SourceLine 0
        try {
            // BEGIN CatchingErrors INNER PART
            // BEGIN CatchingErrors_Begin INNER PART
            // ENDOF CatchingErrors_Begin INNER PART
            // JavaLine 26 <== SourceLine 10
            bf_1.inbyte();
            // ENDOF CatchingErrors INNER PART
            // JavaLine 29 <== SourceLine 0
        } catch(RuntimeException e) { _CUR=this; _onError(e,onError_0()); }
            EBLK();
            return(this);
        } // End of Class Statements
        public static RTS_PROGINFO _INFO=new RTS_PROGINFO("C:/GitHub/WorkSpaces/Eclipse/SimulaLanguageServer/SimulaTestBatch/src/simulaTestBatch/adHoc05.sim","PrefixedBlock CatchingErrors_Begin",1,3,21,0,26,10,29,0,33,3);
    } // End of Class
