// JavaLine 1 <== SourceLine 23
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 09:05:59 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst104_SimulaTestBegin_P extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=23, lastLine=28, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine -25
    public int _RESULT=0;
    // Normal Constructor
    public simtst104_SimulaTestBegin_P(RTS_RTObject _SL) {
        super(_SL);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst104_SimulaTestBegin_P _STM() {
        // JavaLine 25 <== SourceLine 27
        _RESULT=34;
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst104.sim","Procedure P",1,23,12,-25,25,27,30,28);
} // End of Procedure
