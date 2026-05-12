// JavaLine 1 <== SourceLine 9
package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 12:09:28 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class MESSAGE extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=9, lastLine=9, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public int p__SW;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine -23
    public RTS_LABEL _RESULT=null;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public MESSAGE setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p__SW=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public MESSAGE(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public MESSAGE(RTS_RTObject _SL,int sp__SW) {
        super(_SL);
        // Parameter assignment to locals
        this.p__SW = sp__SW;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Switch Body
    @Override
    public MESSAGE _STM() {
        switch(p__SW-1) {
            case 0: _RESULT=((Precompiled129_A)(_CUR._SL))._LABEL_Precompiled129_A_M1_0; break;
            case 1: _RESULT=((Precompiled129_A)(_CUR._SL))._LABEL_Precompiled129_A_M2_0; break;
            case 2: _RESULT=((Precompiled129_A)(_CUR._SL))._LABEL_Precompiled129_A_M3_0; break;
            default: throw new RTS_SimulaRuntimeError("Illegal switch index: "+p__SW);
        }
        EBLK();
        return(this);
    } // End of Switch BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("Precompiled129.sim","Procedure MESSAGE",1,9,13,-23,51,9);
} // End of Procedure
