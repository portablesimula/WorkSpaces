// JavaLine 1 <== SourceLine 3
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:24:49 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_SimulationBegin_startup extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=3, lastLine=5, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_SimulationBegin_startup(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_SimulationBegin_startup _STM() {
        // JavaLine 21 <== SourceLine 4
        ((adHoc00_SimulationBegin)(_CUR._SL)).ActivateAt(true,(RTS_Process)((adHoc00_SimulationBegin)(_CUR._SL)).main,(((adHoc00_SimulationBegin)(_CUR._SL)).time()+(20.0d)),false);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure startup",1,3,21,4,25,5);
} // End of Procedure
