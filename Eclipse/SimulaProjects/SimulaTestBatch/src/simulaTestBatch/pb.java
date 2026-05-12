// JavaLine 1 <== SourceLine 1
package simulaTestBatch;
// Simula-2.0 Compiled at Tue May 12 10:50:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class pb extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=1, firstLine=1, lastLine=6, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public int p_n;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine 4
    public int tull=0;
    // JavaLine 15 <== SourceLine -23
    public int _RESULT=0;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public pb setPar(Object param) {
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
    public pb(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public pb(RTS_RTObject _SL,int sp_n) {
        super(_SL);
        // Parameter assignment to locals
        this.p_n = sp_n;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public pb _STM() {
        // JavaLine 45 <== SourceLine 4
        tull=444;
        // JavaLine 47 <== SourceLine 5
        if(_VALUE((p_n<(10)))) {
            _RESULT=RTS_UTIL._IADD(p_n,new pa(_USR,RTS_UTIL._IADD(p_n,1))._RESULT);
        }
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("p40c.sim","Procedure pb",1,1,13,4,15,-23,45,4,47,5,53,6);
} // End of Procedure
