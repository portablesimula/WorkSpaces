// JavaLine 1 <== SourceLine 80
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107_SimulaTestBegin_SimulationBegin_getime extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=80, lastLine=85, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine -25
    public float _RESULT=0.0f;
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin_getime(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_getime _STM() {
        // JavaLine 25 <== SourceLine 83
        _RESULT=((float)(RTS_TXT.getreal(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).actime_2)));
        ;
        // JavaLine 28 <== SourceLine 84
        ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).actime_2=RTS_TXT.sub(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).actime_2,RTS_TXT.pos(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).actime_2),RTS_UTIL._IADD(RTS_UTIL._ISUB(RTS_TXT.length(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).actime_2),RTS_TXT.pos(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).actime_2)),1));
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","Procedure getime",1,80,12,-25,25,83,28,84,33,85);
} // End of Procedure
