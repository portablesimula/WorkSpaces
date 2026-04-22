// JavaLine 1 <== SourceLine 18
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 09:05:59 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst104_SimulaTestBegin_trace_Block18 extends RTS_BASICIO {
    // SubBlock: Kind=5, BlockLevel=3, firstLine=18, lastLine=22, hasLocalClasses=false, System=false
    // Declare locals as attributes
    // JavaLine 9 <== SourceLine 19
    public final RTS_TXT _case;
    // Normal Constructor
    public simtst104_SimulaTestBegin_trace_Block18(RTS_RTObject staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
        _case=CONC(CONC(CONC(CONC(((simtst104_SimulaTestBegin_trace)(_CUR._SL)).p_msg,new RTS_TXT(", Result=")),RTS_ENVIRONMENT.edit(((simtst104_SimulaTestBegin_trace)(_CUR._SL)).p_r)),new RTS_TXT("  Facit=")),RTS_ENVIRONMENT.edit(((simtst104_SimulaTestBegin_trace)(_CUR._SL)).p_facit));
    }
    // 5 Statements
    @Override
    public RTS_RTObject _STM() {
        // JavaLine 21 <== SourceLine 20
        if(_VALUE((((simtst104_SimulaTestBegin_trace)(_CUR._SL)).p_r!=(((simtst104_SimulaTestBegin_trace)(_CUR._SL)).p_facit)))) {
            new SimulaTest_err((_CUR._SL._SL),_case);
        } else {
            RTS_BASICIO.sysout().outtext(CONC(new RTS_TXT("TRACE: "),_case));
        }
        ;
        // JavaLine 28 <== SourceLine 18
        RTS_BASICIO.sysout().outimage();
        ;
        EBLK();
        return(this);
    } // End of 5 Statements
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst104.sim","SubBlock Block18",1,18,9,19,21,20,28,18,33,22);
} // End of SubBlock
