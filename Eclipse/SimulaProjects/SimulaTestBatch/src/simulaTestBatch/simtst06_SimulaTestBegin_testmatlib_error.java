// JavaLine 1 <== SourceLine 25
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_error extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=25, lastLine=26, hasLocalClasses=false, System=false
    // Declare parameters as attributes
    public RTS_TXT p_t;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_error setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 1: p_t=RTS_ENVIRONMENT.copy((RTS_TXT)objectValue(param)); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_error(RTS_RTObject _SL) {
        super(_SL,1); // Expecting 1 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_error(RTS_RTObject _SL,RTS_TXT sp_t) {
        super(_SL);
        // Parameter assignment to locals
        this.p_t = sp_t;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_error _STM() {
        // JavaLine 39 <== SourceLine 26
        new SimulaTest_err((_CUR._SL._SL),CONC(p_t,new RTS_TXT(" routine")));
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure error",1,25,39,26,43,26);
} // End of Procedure
