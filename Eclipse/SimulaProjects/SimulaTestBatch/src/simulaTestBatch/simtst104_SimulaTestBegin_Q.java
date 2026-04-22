// JavaLine 1 <== SourceLine 28
package simulaTestBatch;
// Simula-2.0 Compiled at Wed Apr 15 09:05:59 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst104_SimulaTestBegin_Q extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=2, firstLine=28, lastLine=33, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public RTS_NAME<RTS_PRCQNT> p_F;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine -25
    public float _RESULT=0.0f;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst104_SimulaTestBegin_Q setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_F=(RTS_NAME<RTS_PRCQNT>)param; break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst104_SimulaTestBegin_Q(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst104_SimulaTestBegin_Q(RTS_RTObject _SL,RTS_NAME<RTS_PRCQNT> sp_F) {
        super(_SL);
        // Parameter assignment to locals
        this.p_F = sp_F;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst104_SimulaTestBegin_Q _STM() {
        // JavaLine 43 <== SourceLine 32
        _RESULT=((float)(intValue(p_F.get().CPF()._RESULT())));
        ;
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst104.sim","Procedure Q",1,28,13,-25,43,32,48,33);
} // End of Procedure
