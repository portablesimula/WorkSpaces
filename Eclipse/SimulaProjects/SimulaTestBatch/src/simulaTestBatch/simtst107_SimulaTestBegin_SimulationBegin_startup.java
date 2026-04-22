// JavaLine 1 <== SourceLine 33
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 10:34:34 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst107_SimulaTestBegin_SimulationBegin_startup extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=33, lastLine=36, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public simtst107_SimulaTestBegin_SimulationBegin_startup(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst107_SimulaTestBegin_SimulationBegin_startup _STM() {
        new simtst107_SimulaTestBegin_SimulationBegin_outstate((_CUR._SL),0);
        ;
        // JavaLine 23 <== SourceLine 36
        ((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).ActivateAt(true,(RTS_Process)((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).main,(((simtst107_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time()+(20.0d)),false);
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst107.sim","Procedure startup",1,33,23,36,28,36);
} // End of Procedure
