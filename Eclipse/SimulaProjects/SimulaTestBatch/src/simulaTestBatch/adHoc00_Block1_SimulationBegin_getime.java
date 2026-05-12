// JavaLine 1 <== SourceLine 32
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:22:53 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_SimulationBegin_getime extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=32, lastLine=35, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine -23
    public float _RESULT=0.0f;
    // Normal Constructor
    public adHoc00_Block1_SimulationBegin_getime(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_Block1_SimulationBegin_getime _STM() {
        // JavaLine 25 <== SourceLine 33
        _RESULT=((float)(RTS_TXT.getreal(((adHoc00_Block1_SimulationBegin)(_CUR._SL)).actime_2)));
        // JavaLine 27 <== SourceLine 34
        ((adHoc00_Block1_SimulationBegin)(_CUR._SL)).actime_2=RTS_TXT.sub(((adHoc00_Block1_SimulationBegin)(_CUR._SL)).actime_2,RTS_TXT.pos(((adHoc00_Block1_SimulationBegin)(_CUR._SL)).actime_2),RTS_UTIL._IADD(RTS_UTIL._ISUB(RTS_TXT.length(((adHoc00_Block1_SimulationBegin)(_CUR._SL)).actime_2),RTS_TXT.pos(((adHoc00_Block1_SimulationBegin)(_CUR._SL)).actime_2)),1));
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure getime",1,32,12,-23,25,33,27,34,31,35);
} // End of Procedure
