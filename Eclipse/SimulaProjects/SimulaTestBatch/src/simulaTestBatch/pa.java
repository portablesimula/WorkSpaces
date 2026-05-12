// JavaLine 1 <== SourceLine 1
package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 10:50:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class pa extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=1, firstLine=1, lastLine=5, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public int p_n;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine -23
    public int _RESULT=0;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public pa setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_n=intValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public pa(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public pa(RTS_RTObject _SL,int sp_n) {
        super(_SL);
        // Parameter assignment to locals
        this.p_n = sp_n;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public pa _STM() {
        // JavaLine 43 <== SourceLine 4
        if(_VALUE((p_n<(10)))) {
            _RESULT=RTS_UTIL._IADD(p_n,new pb(_USR,RTS_UTIL._IADD(p_n,1))._RESULT);
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("p40a.sim","Procedure pa",1,1,13,-23,43,4,49,5);
} // End of Procedure
