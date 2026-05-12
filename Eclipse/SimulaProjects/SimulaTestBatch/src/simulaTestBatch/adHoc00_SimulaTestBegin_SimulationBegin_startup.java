// JavaLine 1 <== SourceLine 27
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:21:55 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulaTestBegin_SimulationBegin_startup extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=27, lastLine=28, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_SimulaTestBegin_SimulationBegin_startup(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_SimulaTestBegin_SimulationBegin_startup _STM() {
        // JavaLine 21 <== SourceLine 28
        ((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).ActivateAt(true,(RTS_Process)((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).main,(((adHoc00_SimulaTestBegin_SimulationBegin)(_CUR._SL)).time()+(20.0d)),false);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure startup",1,27,21,28,25,28);
} // End of Procedure
