// JavaLine 1 <== SourceLine 71
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107_SimulaTestBegin_SimulationBegin_createobjects extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=71, lastLine=80, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 10 <== SourceLine 74
    public int i=0;
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin_createobjects(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_createobjects _STM() {
        for(i=1;i<=10;i++) {
            // JavaLine 24 <== SourceLine 76
            {
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).pa.index(i),((simtst107_SimulaTestBegin_SimulationBegin_p)new simtst107_SimulaTestBegin_SimulationBegin_p((_CUR._SL),i)._START()));
                ;
                // JavaLine 28 <== SourceLine 77
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).terminatd.index(i),false);
                ;
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).active.index(i),false);
                ;
                ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.putELEMENT(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).passive.index(i),true);
                ;
            }
        }
        ;
        // JavaLine 38 <== SourceLine 79
        ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).testno_2=RTS_UTIL._IADD(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).testno_2,1);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","Procedure createobjects",1,71,10,74,24,76,28,77,38,79,42,80);
} // End of Procedure
