// JavaLine 1 <== SourceLine 4
package simulaTestBatch;
// Simula-2.0 Compiled at Sun May 10 11:24:31 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class adHoc00_Block1_SimulationBegin_startup extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=4, lastLine=6, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public adHoc00_Block1_SimulationBegin_startup(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public adHoc00_Block1_SimulationBegin_startup _STM() {
        // JavaLine 21 <== SourceLine 5
        ((adHoc00_Block1_SimulationBegin)(_CUR._SL)).ActivateAt(true,(RTS_Process)((adHoc00_Block1_SimulationBegin)(_CUR._SL)).main,(((adHoc00_Block1_SimulationBegin)(_CUR._SL)).time()+(20.0d)),false);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("adHoc00.sim","Procedure startup",1,4,21,5,25,6);
} // End of Procedure
