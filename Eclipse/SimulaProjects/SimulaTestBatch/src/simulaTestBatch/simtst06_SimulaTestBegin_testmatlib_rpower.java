// JavaLine 1 <== SourceLine 57
package simulaTestBatch;
// Simula-2.0 Compiled at Wed May 06 09:31:20 CEST 2026
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst06_SimulaTestBegin_testmatlib_rpower extends RTS_PROCEDURE {
    // ProcedureDeclaration: Kind=6, BlockLevel=3, firstLine=57, lastLine=58, hasLocalClasses=false, System=false
    @Override
public Object _RESULT() { return(_RESULT); }
    // Declare parameters as attributes
    public double p_x;
    public double p_y;
    // Declare locals as attributes
    // JavaLine 14 <== SourceLine -23
    public double _RESULT=0.0d;
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    @Override
    public simtst06_SimulaTestBegin_testmatlib_rpower setPar(Object param) {
        try {
            switch(_nParLeft--) {
                case 2: p_x=doubleValue(param); break;
                case 1: p_y=doubleValue(param); break;
                default: throw new RTS_SimulaRuntimeError("Too many parameters");
            }
        }
    catch(ClassCastException e) { throw new RTS_SimulaRuntimeError("Wrong type of parameter: "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public simtst06_SimulaTestBegin_testmatlib_rpower(RTS_RTObject _SL) {
        super(_SL,2); // Expecting 2 parameters
    }
    // Normal Constructor
    public simtst06_SimulaTestBegin_testmatlib_rpower(RTS_RTObject _SL,double sp_x,double sp_y) {
        super(_SL);
        // Parameter assignment to locals
        this.p_x = sp_x;
        this.p_y = sp_y;
        BBLK();
        // Declaration Code
        _STM();
    }
    // Procedure Statements
    @Override
    public simtst06_SimulaTestBegin_testmatlib_rpower _STM() {
        // JavaLine 46 <== SourceLine 58
        _RESULT=RTS_ENVIRONMENT.exp((p_y*(RTS_ENVIRONMENT.ln(p_x))));
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static RTS_PROGINFO _INFO=new RTS_PROGINFO("simtst06.sim","Procedure rpower",1,57,14,-23,46,58,50,58);
} // End of Procedure
