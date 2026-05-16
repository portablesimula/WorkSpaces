// JavaLine 1 <== SourceLine 25
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 13 08:12:35 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst129_Precompiled129Begin_test extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=25, lastLine=30, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public simtst129_Precompiled129Begin_test(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst129_Precompiled129Begin_test _STM() {
        // JavaLine 21 <== SourceLine 26
        new simtst129_Precompiled129Begin_trace((_CUR._SL),new Precompiled129_A((_CUR._SL),2)._STM().mess);
        // JavaLine 23 <== SourceLine 27
        new simtst129_Precompiled129Begin_trace((_CUR._SL),new Precompiled129_A((_CUR._SL),1)._STM().mess);
        // JavaLine 25 <== SourceLine 28
        new simtst129_Precompiled129Begin_trace((_CUR._SL),new Precompiled129_A((_CUR._SL),3)._STM().mess);
        // JavaLine 27 <== SourceLine 29
        new simtst129_Precompiled129Begin_trace((_CUR._SL),new Precompiled129_A((_CUR._SL),99)._STM().mess);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst129.sim","Procedure test",1,25,21,26,23,27,25,28,27,29,31,30);
} // End of Procedure
